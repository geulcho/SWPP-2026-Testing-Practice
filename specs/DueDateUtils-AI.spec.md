# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the task list label from the calendar-day distance between today and the due date. Completion takes precedence over the due date.

**Pre-conditions**
- `today` is the reference date; `dueDate` may be null.

**Post-conditions**
- Success cases
  - A completed task with a due date returns `"CLEAR"`. [Unit]
    - Tip: Given `dueDate = 2026-10-19`, `today = 2026-10-14`, and `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - A completed task without a due date returns `"CLEAR"`. [Unit]
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, and `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - An active task without a due date returns an empty label. [Unit]
    - Tip: Given `dueDate = null`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `""`.
  - An active task due in five days returns `"D-5"`. [Unit]
    - Tip: Given `dueDate = 2026-10-19`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-5"`.
  - An active task overdue by five days returns `"D+5"`. [Unit]
    - Tip: Given `dueDate = 2026-10-09`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+5"`.
  - A full month or year is counted in calendar days. [Unit]
    - Tip: Given `dueDate = 2026-11-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-31"`.
    - Tip: Given `dueDate = 2027-10-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-365"`.
- Error cases
  - None.
- Edge cases
  - The day before, the due date, and the day after the D-day boundary return their respective labels. [Unit]
    - Tip: Given `dueDate = 2026-10-13`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+1"`.
    - Tip: Given `dueDate = 2026-10-14`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
    - Tip: Given `dueDate = 2026-10-15`, `today = 2026-10-14`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - Month-end counting remains continuous across October 31. [Unit]
    - Tip: Given `dueDate = 2026-10-31`, `today = 2026-10-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
    - Tip: Given `dueDate = 2026-11-01`, `today = 2026-10-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
    - Tip: Given `dueDate = 2026-11-02`, `today = 2026-10-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-2"`.
  - Year-end counting remains continuous across December 31. [Unit]
    - Tip: Given `dueDate = 2026-12-31`, `today = 2026-12-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
    - Tip: Given `dueDate = 2027-01-01`, `today = 2026-12-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
    - Tip: Given `dueDate = 2027-01-02`, `today = 2026-12-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-2"`.
  - Leap-day counting remains continuous across February 29. [Unit]
    - Tip: Given `dueDate = 2024-02-28`, `today = 2024-02-28`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
    - Tip: Given `dueDate = 2024-02-29`, `today = 2024-02-28`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
    - Tip: Given `dueDate = 2024-03-01`, `today = 2024-02-28`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-2"`.
