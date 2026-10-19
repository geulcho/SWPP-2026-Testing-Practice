# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Show the current task's due-date label using the device's current local date. Clear and hide the label when the task has no label, including when a view is rebound from another task.

**Pre-conditions**
- `textView` is a valid Android `TextView` on the UI thread; `task` may be null.

**Post-conditions**
- Success cases
  - An active task with a due date displays the day-count label and makes the view visible. [Integration]
    - Tip: Given the device local date is `2026-10-14`, `textView.text = "OLD"`, `textView.visibility = View.GONE`, and `task = Task(title = "Pay rent", dueDate = 2026-10-19, isCompleted = false)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text = "D-5"` and `textView.visibility = View.VISIBLE`.
  - A completed task without a due date displays `"CLEAR"` and makes the view visible. [Integration]
    - Tip: Given the device local date is `2026-10-14`, `textView.text = "OLD"`, `textView.visibility = View.GONE`, and `task = Task(title = "Pay rent", dueDate = null, isCompleted = true)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text = "CLEAR"` and `textView.visibility = View.VISIBLE`.
  - An active task without a due date clears the text and hides the view. [Integration]
    - Tip: Given the device local date is `2026-10-14`, `textView.text = "D-5"`, `textView.visibility = View.VISIBLE`, and `task = Task(title = "Pay rent", dueDate = null, isCompleted = false)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text = ""` and `textView.visibility = View.GONE`.
  - A null task clears the text and hides the view. [Integration]
    - Tip: Given the device local date is `2026-10-14`, `textView.text = "CLEAR"`, `textView.visibility = View.VISIBLE`, and `task = null` / When `setDueDateLabel(textView, task)` is called / Then `textView.text = ""` and `textView.visibility = View.GONE`.
- Error cases
  - None.
- Edge cases
  - Due yesterday, today, and tomorrow give the labels on both sides of the D-day boundary and at the boundary. [Integration]
    - Tip: Given the device local date is `2026-10-14`, `textView.text = "OLD"`, `textView.visibility = View.GONE`, and `task = Task(title = "Pay rent", dueDate = 2026-10-13, isCompleted = false)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text = "D+1"` and `textView.visibility = View.VISIBLE`.
    - Tip: Given the device local date is `2026-10-14`, `textView.text = "OLD"`, `textView.visibility = View.GONE`, and `task = Task(title = "Pay rent", dueDate = 2026-10-14, isCompleted = false)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text = "D-Day"` and `textView.visibility = View.VISIBLE`.
    - Tip: Given the device local date is `2026-10-14`, `textView.text = "OLD"`, `textView.visibility = View.GONE`, and `task = Task(title = "Pay rent", dueDate = 2026-10-15, isCompleted = false)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text = "D-1"` and `textView.visibility = View.VISIBLE`.
