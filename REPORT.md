# Testing Practice Report

## 1. Repository

- Forked repository: https://github.com/geulcho/SWPP-2026-Testing-Practice
- Pull request: https://github.com/geulcho/SWPP-2026-Testing-Practice/pull/1
- Reviewed branch: `feature/due-date`; latest checked commit: `4fc90ce`.

## 2. Spec Refinements

The initial AI-generated specifications were preserved as `DueDateUtils-AI.spec.md` and `TasksListBindings-AI.spec.md`. I reviewed the expected behavior against the pull-request description and the function contract; the current implementation contains the two bugs investigated below.

### 2.1 `DueDateUtils.spec.md`

#### Refinement 1: Separate month- and year-spanning calculations

The AI draft grouped a full-month case and a full-year case into one post-condition. I separated them into independent success cases with exact dates and expected labels:

- `2026-10-14` to `2026-11-14` must produce `D-31`.
- `2026-10-14` to `2027-10-14` must produce `D-365`.

This makes each oracle independently traceable to one test and explicitly checks that the implementation uses the total calendar-day distance. These cases are important because an implementation that uses only the day component of a period can return an incorrect result when the interval crosses a month or year boundary.

The existing draft already covered completed tasks, null due dates, future and overdue dates, the D-day boundary, month and year boundaries, and leap-day behavior, so those cases were retained.

### 2.2 `TasksListBindings.spec.md`

#### Refinement 2: Cover a completed task that has a due date

The AI draft covered a completed task without a due date, but the feature intent states that every completed task must display `CLEAR`, regardless of whether a due date exists. I added an integration case in which a completed task has the due date `2026-10-19`. The expected result is `textView.text = "CLEAR"` and `textView.visibility = View.VISIBLE`.

This refinement verifies that completion takes precedence over the due-date label and prevents the binding layer from displaying a `D-N` label for a completed task.

#### Refinement 3: Reproduce the stale-label state transition

The AI draft checked that an active task without a due date clears an old `D-5` label. I changed the initial label to `CLEAR` and kept the view visible. This reproduces the binding state seen after a task with no due date is completed and then unchecked. The test prepares the `TextView` in that state; it does not click the checkbox.

After rebinding the now-active task, the expected result is `textView.text = ""` and `textView.visibility = View.GONE`. This case directly detects the known defect where `CLEAR` remains visible after the task is unchecked.

## 3. Tests and Local Results

The reviewed specs contain 19 JVM Tips and 8 Android integration Tips. The agent wrote one test per Tip: 19 tests in `DueDateUtilsTest.kt` and 8 in `TasksListBindingsTest.kt`. Production code was not changed during test generation. The local result records from October 14, 2026 selected these two test classes using the following Gradle task arguments on Windows (JDK 21 and Gradle 8.13):

```text
:app:testDebugUnitTest --tests com.example.swpp.tododapp.tasks.DueDateUtilsTest
:app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.swpp.tododapp.tasks.TasksListBindingsTest
```

The JVM run used Gradle offline mode; the Android run did not because its desugaring dependency was absent from the offline cache. These local commands selected the new classes, not the full suite. The local Android result identifies the emulator as `Medium_Phone(AVD) - 16`.

| Test group | Passed | Failed | What the failures show |
| --- | ---: | ---: | --- |
| JVM unit tests | 17 | 2 | The month- and year-spanning cases return the wrong D-day value. |
| Android instrumented tests | 6 | 2 | A previously displayed `CLEAR` label is not cleared when there is no current label. |

### Locally failed cases

1. `dueDateLabel_dueOneMonthLater_returnsDMinus31`: for `today = 2026-10-14` and `dueDate = 2026-11-14`, the expected label is `D-31`, but the agent reported `D-Day`.
2. `dueDateLabel_dueOneYearLater_returnsDMinus365`: for `today = 2026-10-14` and `dueDate = 2027-10-14`, the expected label is `D-365`, but the agent reported `D-Day`.
3. `setDueDateLabel_activeWithoutDate_clearsStaleLabel`: an active task with no due date is bound to a `TextView` initially showing `CLEAR` with `View.VISIBLE`. Expected text `""`; actual text `"CLEAR"`.
4. `setDueDateLabel_nullTask_clearsStaleLabel`: a null task is bound to a `TextView` initially showing `CLEAR` with `View.VISIBLE`. Expected text `""`; actual text `"CLEAR"`.

The first two failures detect the same date-calculation defect: `Period.between(today, dueDate).days` uses the remaining day component, rather than the total calendar-day distance. The last two detect the stale-text defect: `setDueDateLabel()` updates the view only when the new label is nonempty, leaving `CLEAR` on a reused view. These four failing tests expose two distinct production bugs. The remaining 23 new tests passed locally. The Android tests assert text before visibility, so the failures prevent the `View.GONE` assertions from running; these runs do not independently establish the final visibility.

## 4. Pull Request CI Results

I checked the completed `Android Tests` workflow for commit `4fc90ce` on pull request #1, after the four specs and tests were pushed. GitHub Actions ran `./gradlew testDebugUnitTest` and `./gradlew connectedDebugAndroidTest`. Both jobs finished with assertion failures:

| CI job | Executed | Failed | Visible failure evidence |
| --- | ---: | ---: | --- |
| `unit-test` | 20 | 2 | `dueDateLabel_dueOneYearLater_returnsDMinus365`: expected `D-365`, actual `D-Day`; `dueDateLabel_dueOneMonthLater_returnsDMinus31`: expected `D-31`, actual `D-Day`. |
| `integration-test` | 9 | 2 | `setDueDateLabel_activeWithoutDate_clearsStaleLabel` and `setDueDateLabel_nullTask_clearsStaleLabel`: in both cases, expected text `""`, actual text `"CLEAR"`. |

The CI jobs each include one pre-existing test in addition to the corresponding new test class: 18 of 20 JVM tests passed and 7 of 9 Android tests passed. The two JVM failures confirm the date-distance bug, and both Android failures confirm that a stale `CLEAR` label is not cleared. The emulator teardown messages appear after the assertion failures and are not evidence of a separate test failure. The PR's failing CI is the observed result of tests exposing the feature's intentional bugs; it does not imply that the tests failed to compile or run.

## 5. Coverage Limitations and Conclusion

The Android tests use `LocalDate.now()` and relative offsets rather than fixing the device date to the exact `2026-10-14` examples in the reviewed spec. They test the intended day differences, but do not reproduce that exact device date. Their text assertions fail before their visibility assertions run, so the observed results specifically establish stale text, while `View.GONE` remains part of the intended contract. All four specs and both generated test files are present in the pull request. Across the local and CI runs, the reviewed specs detected both known production bugs without changing the production implementation.
