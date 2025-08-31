# ✅ Daily Task Manager

**Daily Task Manager** is a simple Android app (Kotlin) designed to help users **organize daily tasks efficiently**.  
The app comes with a **countdown timer** that resets daily, automatic **rest periods**, and real-time **progress tracking** based on completed tasks.  

It provides a minimal and intuitive UI to **add, edit, delete, and pin tasks**, making it an ideal companion for productivity.

---

## 📱 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/3b1fa635-b483-486c-a690-cd9c18a00e4c" alt="Main Screen with Timer" width="250"/>
  <img src="https://github.com/user-attachments/assets/015b3cbb-75c6-440a-a393-7efc9c07838e" alt="Add New Task Screen" width="250"/>
  <img src="https://github.com/user-attachments/assets/9b2cc318-5f9c-40e8-8938-e5bbe477c39e" alt="Task List Screen" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/5e3ed040-14b7-441b-a76c-2c7bc2774fe4" alt="Task Completion 50%" width="250"/>
  <img src="https://github.com/user-attachments/assets/b53d91cd-9a13-4a0b-ad9b-6540a0bcc0a1" alt="Rest Mode Screen" width="250"/>
  <img src="https://github.com/user-attachments/assets/0a38b038-75bc-46ae-a149-d898a29b7407" alt="Updated Main Timer" width="250"/>
</p>

---

## ✨ Features
- ⏱ **Daily Countdown Timer**
  - Starts at the beginning of the day and counts down.
  - Automatically enters a **rest mode** once the timer finishes.
  - After the rest period, the cycle restarts and all tasks reset.

- 📝 **Task Management**
  - Add new tasks with a simple input form.
  - Edit task titles with an inline dialog.
  - Delete tasks permanently with one click.
  - Pin tasks to keep them highlighted in the task list.

- ✅ **Progress Tracking**
  - Each completed task increases the overall **completion percentage**.
  - Progress is updated in real-time on both the **Task List** and **Main Screen**.

- 📊 **Completion Percentage**
  - Displays what percentage of tasks have been completed.
  - Automatically recalculates after any edit, delete, or check/uncheck.

- 💤 **Rest Mode**
  - After the timer finishes, the app enters a rest state (e.g., "Rest: 28s").
  - During rest mode, task creation is disabled until the next cycle.

- 🎨 **Modern UI**
  - Gradient backgrounds for each screen.
  - Large, bold typography for timers and headers.
  - Simple buttons with consistent styling.

---

## 🛠 Tech Stack
- **Language:** Kotlin (Android)  
- **UI:** ConstraintLayout, LinearLayout, RecyclerView  
- **Data Storage:** JSON file (`tasks.json`) stored locally in internal storage  
- **Main Components:**
  - `MainActivity` → Countdown timer & overall progress
  - `AddTaskActivity` → Add new tasks
  - `TaskListActivity` → List, edit, pin, and delete tasks
  - `TaskAdapter` → RecyclerView adapter for task items
  - `Task` → Data model class

---

## 📂 Project Structure
```plaintext
app/
 ├─ src/main/java/com/example/dailytaskmanager/
 │   ├─ MainActivity.kt          # Main screen with timer and completion percentage
 │   ├─ AddTaskActivity.kt       # Add new tasks
 │   ├─ TaskListActivity.kt      # Task list with edit, delete, and pin support
 │   ├─ TaskAdapter.kt           # RecyclerView adapter for displaying tasks
 │   └─ Task.kt                  # Data model for a task (title + isCompleted)
 │
 ├─ res/layout/
 │   ├─ activity_main.xml        # Layout for main screen
 │   ├─ activity_add_task.xml    # Layout for adding new task
 │   ├─ activity_task_list.xml   # Layout for listing tasks
 │   └─ task_item.xml            # Layout for each task item
 │
 ├─ res/drawable/                # Gradient backgrounds and custom styles
 ├─ res/values/                  # Strings, colors, themes
 └─ README.md

📊 Data Model
The app uses a simple Task model:

kotlin
data class Task(
    var title: String,
    var isCompleted: Boolean
)
Each task is stored in a local JSON file (tasks.json) in internal storage.

📝 Task Storage (JSON Format)
Here’s how tasks are saved:

json
[
  {
    "title": "Buy groceries",
    "isCompleted": false
  },
  {
    "title": "Finish homework",
    "isCompleted": true
  }
]
✅ title: The text of the task

✅ isCompleted: Boolean to track completion

When you edit, add, delete, or check a task, the file is updated automatically.
