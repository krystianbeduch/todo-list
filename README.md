# To-Do List
The project was developed as part of my IT studies during a course on mobile application design.

The application is a task planner designed to help you organize your daily activities. You can assign priorities to tasks, set deadlines, mark tasks as completed, and attach files. A notification mechanism reminds you of upcoming deadlines and alerts you to tasks you have not completed on time.

### Contents
1. [Application functionality](#application-functionality)
2. [Technology](#technology)
3. [Setup](#setup)
4. [Functional description](#functional-description)
   - [List of tasks](#list-of-tasks)
   - [Adding a new task](#adding-a-new-task)
   - [Editing a task from the list](#editing-a-task-from-the-list)
   - [Managing tasks](#managing-tasks)
   - [Managing attachments](#managing-attachments)
   - [Notification mechanism](#notification-mechanism)
   - [Import and export of task list to files](#import-and-export-of-task-list-to-files)

## Application functionality
- Displays all saved tasks in a clear list, allowing easy browsing and access to task details
- Users can quickly add a new task by specifying its title, priority and deadline
- List tasks can be edited to update their titles, deadlines or priorities at any time
- Task management - marking tasks as completed, deleting tasks or sorting tasks based on date added, title, priority, task status or deadline
- User can add attachments (eg. images, videos or PDF documents) to tasks, view them or delete them as needed
- A notification mechanism sends reminders about upcoming deadlines and alerts the user when a task is overdue
- Import and export task list to files allows you to save the task list to a file or upload new tasks, supporting CSV, JSON and XML file formats

## Technology
- Android SDK 35 (min. SDK 27)
- Java 21
- Room 2.7.1
- Gson 2.13.1
- Simple XML 2.7.1
- Lombok 1.18.38
- Gradle Plugin AGP 8.8.2 (Kotlin)

## Setup  
### 1. Configure the Android Studio environment
> [!NOTE]
> This project was developed using the _Ladybug_ version of Android Studio.
> You do not need to install a separate JDK, as Android Studio includes its own buit-in JDK.

### 2. Download or clone the project
- Option 1: Download the ZIP file from github and extract it
- Option 2: Clone the repository:
```bash
git clone https://github.com/krystianbeduch/todo-list.git
```

### 3. Open the project in Android Studio
- Select `Open` and choose the project folder
- Wait for Gradle to finish syncing all dependencies

### 4. Run the application
- Ensure you have a connected __emulator__ or __physical Android device__ with __debugging enabled__
- Press __Run__ (`Shift + F10` or the green button) to launch the app

