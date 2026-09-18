# StudyMate Pro - Testing Plan

## Testing Strategy

Testing combines:

1. **Dependency-free self-tests** for core business rules.
2. **Manual functional tests** for Swing forms and navigation.
3. **Persistence testing** by restarting the application after changes.
4. **Validation testing** using invalid and boundary inputs.

## Automated Self-Test

`test/StudyMateSelfTest.java` currently verifies:

- duplicate subject validation,
- required task-field validation,
- task completion percentage,
- goal progress percentage,
- seven-day habit consistency,
- recommended-task priority engine.

Expected result: all tests pass with exit code 0.

## Manual Test Cases

| ID | Test | Expected Result |
|---|---|---|
| T01 | Add valid subject | Subject appears in table |
| T02 | Add duplicate subject code | Validation warning is shown |
| T03 | Edit subject | Selected record is updated |
| T04 | Delete subject | Selected record is removed |
| T05 | Add valid task | Task appears in table |
| T06 | Add task with empty title | Validation warning is shown |
| T07 | Add task with invalid date | Date validation warning is shown |
| T08 | Edit task | Selected task is updated |
| T09 | Mark task completed | Status becomes Completed |
| T10 | Delete task | Selected task is removed |
| T11 | Add/edit/delete note | Table reflects each operation |
| T12 | Add/delete schedule item | Timetable data changes correctly |
| T13 | Add/complete/delete reminder | Reminder state updates correctly |
| T14 | Add/edit/delete exam | Exam data and countdown update |
| T15 | Add goal and increment progress | Progress bar updates and is capped at target |
| T16 | Add habit and toggle today | Weekly log changes |
| T17 | Open Progress & Reports | Live metrics are calculated |
| T18 | Open Achievements | Milestones reflect current activity |
| T19 | Save data, restart app | Saved records remain available |
| T20 | Reset sample data | Demo dataset replaces current records |

## Boundary / Error Tests

- Negative goal progress -> rejected/capped.
- Zero target -> rejected by positive-integer validation where applicable.
- Empty required text field -> warning.
- Invalid date -> warning.
- Invalid reminder date/time -> warning.
- Delete without selecting a row -> warning.
- Empty dataset -> dashboard shows zero values and no crashes.
