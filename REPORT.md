# Testing Practice Report

## 1. Repository

- Git remote `origin`: https://github.com/geulcho/SWPP-2026-Testing-Practice
- Current local branch: `feature/due-date`.
- This report describes the local working tree checked on October 19, 2026. At that time, `REPORT.md`, the four spec files, and the two new test files were untracked; no tracked production file differed from `HEAD`.

## 2. Spec Refinements

The initial AI-generated specifications were preserved as `DueDateUtils-AI.spec.md` and `TasksListBindings-AI.spec.md`. I reviewed their expected outcomes against the due-date behavior documented in `DueDateUtils.kt` and the task-list binding's purpose. The expected outcomes are not derived from the current return values.

### 2.1 `DueDateUtils.spec.md`

#### Refinement 1: Separate month- and year-spanning calculations

The AI draft grouped a full-month case and a full-year case into one post-condition. I separated them into independent success cases with exact dates and expected labels:

- `2026-10-14` to `2026-11-14` must produce `D-31`.
- `2026-10-14` to `2027-10-14` must produce `D-365`.

This makes each oracle independently traceable to one test and explicitly checks that the implementation uses the total calendar-day distance. These cases are important because an implementation that uses only the day component of a period can return an incorrect result when the interval crosses a month or year boundary.

The existing draft already covered completed tasks, null due dates, future and overdue dates, the D-day boundary, month and year boundaries, and leap-day behavior, so those cases were retained.

### 2.2 `TasksListBindings.spec.md`

#### Refinement 2: Cover a completed task that has a due date

The AI draft covered a completed task without a due date. The `dueDateLabel()` documentation says every completed task displays `CLEAR`, whether or not it has a due date. I added an integration case in which a completed task has the due date `2026-10-19`. The expected result is `textView.text = "CLEAR"` and `textView.visibility = View.VISIBLE`.

This refinement verifies that completion takes precedence over the due-date label and prevents the binding layer from displaying a `D-N` label for a completed task.

#### Refinement 3: Reproduce the stale-label state transition

The AI draft checked that an active task without a due date clears an old `D-5` label. I changed the initial label to `CLEAR` and kept the view visible. The test initializes a `TextView` with `CLEAR` and `View.VISIBLE`, then calls the binding adapter with an active task that has no due date. It does not operate the checkbox.

After rebinding the active task, the expected result is `textView.text = ""` and `textView.visibility = View.GONE`. This case checks whether the binding adapter clears a stale `CLEAR` label.

## 3. Local Test Implementation and Results

The reviewed specs contain 19 JVM Tips and 8 Android integration Tips. `DueDateUtilsTest.kt` has 19 tests and `TasksListBindingsTest.kt` has 8 tests, one for each Tip. The current working tree shows no tracked production-code changes.

The stored local test results are dated October 14, 2026. The runs used JDK 21 and a locally installed Gradle 8.13 on Windows. The Android tests ran on the emulator identified in the result file as `Medium_Phone(AVD) - 16`. The task selections used for these results were:

```text
:app:testDebugUnitTest --tests com.example.swpp.tododapp.tasks.DueDateUtilsTest
:app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.swpp.tododapp.tasks.TasksListBindingsTest
```

The JVM run used Gradle's offline mode. The Android run did not, because its desugaring dependency was not available in the offline cache. These commands select only the two new test classes; they do not describe a run of the entire project test suite. The generated result files are `app/build/test-results/testDebugUnitTest/TEST-com.example.swpp.tododapp.tasks.DueDateUtilsTest.xml` and `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 16-_app-.xml`.

| Test group | Passed | Failed | Observed failure |
| --- | ---: | ---: | --- |
| JVM unit tests | 17 | 2 | Month- and year-spanning dates produced `D-Day`. |
| Android instrumented tests | 6 | 2 | The text remained `CLEAR` when the new binding had no label. |

### Failed cases

1. `dueDateLabel_dueOneMonthLater_returnsDMinus31`: with `today = 2026-10-14` and `dueDate = 2026-11-14`, expected `D-31`; actual `D-Day`.
2. `dueDateLabel_dueOneYearLater_returnsDMinus365`: with `today = 2026-10-14` and `dueDate = 2027-10-14`, expected `D-365`; actual `D-Day`.
3. `setDueDateLabel_activeWithoutDate_clearsStaleLabel`: with an active task that has no due date and a `TextView` initially showing `CLEAR`, expected text `""`; actual text `"CLEAR"`.
4. `setDueDateLabel_nullTask_clearsStaleLabel`: with a null task and a `TextView` initially showing `CLEAR`, expected text `""`; actual text `"CLEAR"`.

The two JVM failures are consistent with the current `dueDateLabel()` implementation using `Period.between(today, dueDate).days`, which omits whole months and years from the total day count. The current `setDueDateLabel()` implementation updates the view only when the new label is nonempty, which explains the stale text in the two Android failures. Each Android test asserts the text before the visibility; because the text assertion failed, those test runs did not reach the visibility assertion. The spec also expects `View.GONE` for these cases, but the XML results alone do not verify the resulting visibility.

The Android tests use `LocalDate.now()` and relative day offsets. They check the specified day differences and labels, but do not use the spec's fixed `2026-10-14` device-date examples. No CI result was inspected for this report, and the local test results do not establish a CI outcome.
