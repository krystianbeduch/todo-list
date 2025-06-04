# To-Do List
The project was developed as part of my IT studies during a course on mobile application design.

The application is a task planner designed to help you organize your daily activities. You can assign priorities to tasks, set deadlines, mark tasks as completed, and attach files. A notification mechanism reminds you of upcoming deadlines and alerts you to tasks you have not completed on time. Application designed based on Model-View-ViewModel (MVVC) design pattern.

### Contents
1. [Application functionality](#application-functionality)
2. [Technology](#technology)
3. [Project structure](#project-structure)
4. [Setup](#setup)
5. [Functional description](#functional-description)
   - [List of tasks](#list-of-tasks)
   - [Adding a new task](#adding-a-new-task)
   - [Editing a task from the list](#editing-a-task-from-the-list)
   - [Managing tasks](#managing-tasks)
   - [Managing attachments](#managing-attachments)
   - [Notification mechanism](#notification-mechanism)
   - [Import and export of task list to files](#import-and-export-of-task-list-to-files)
   - [Changing the application language](#changing-the-application-language)

## Application functionality
- Displays all saved tasks in a clear list, allowing easy browsing and access to task details
- Users can quickly add a new task by specifying its title, priority and deadline
- List tasks can be edited to update their titles, deadlines or priorities at any time
- Task management - marking tasks as completed, deleting tasks or sorting tasks based on date added, title, priority, task status or deadline
- User can add attachments (eg. images, videos or PDF documents) to tasks, view them or delete them as needed
- A notification mechanism sends reminders about upcoming deadlines and alerts the user when a task is overdue
- Import and export task list to files allows you to save the task list to a file or upload new tasks, supporting CSV, JSON and XML file formats
- USer can change the language of the application at any time: Polish and English

## Technology
- Android SDK 35 (min. SDK 27)
- Java 21
- Room 2.7.1
- Gson 2.13.1
- Simple XML 2.7.1
- Lombok 1.18.38
- Gradle Plugin AGP 8.8.2 (Kotlin)

## Project structure
```bash
├───activity
│       EditTaskActivity.java
│       MainActivity.java
│
├───data
│   ├───dao
│   │       AttachmentDao.java
│   │       TaskDao.java
│   │
│   └───db
│           AppDatabase.java
│
├───domain
│   ├───model
│   │   │   Attachment.java
│   │   │   Task.java
│   │   │
│   │   └───enums
│   │           FileType.java
│   │           NotificationType.java
│   │           Priority.java
│   │           SortType.java
│   │
│   └───repository
│           AttachmentRepository.java
│           TaskRepository.java
│
├───presentation
│   ├───addtask
│   │       AddTaskFragment.java
│   │
│   ├───home
│   │   │   HomeFragment.java
│   │   │
│   │   └───adapter
│   │           TaskAdapter.java
│   │
│   ├───notifications
│   │   │   NotificationsFragment.java
│   │   │
│   │   └───adapter
│   │           NotificationAdapter.java
│   │
│   └───viewmodel
│           TaskViewModel.java
│
└───util
    ├───converter
    │       Converters.java
    │
    ├───file
    │   │   FileService.java
    │   │   TaskTypeJsonAdapter.java
    │   │
    │   └───xml
    │           TaskXml.java
    │           TaskXmlWrapper.java
    │
    ├───lang
    │       LocalHelper.java
    │
    ├───notification
    │       NotificationUtils.java
    │
    └───task
            BaseTaskAdapter.java
            TaskFormHelper.java
```
- `activity` - containsa Android Activity classes which are entry points of the user interface, such as the main screen (MainActivity) and task editing screen (EditTaskActivity)
- `data` - data access layer:
   - `dao` - Data Access Object interfaces used for interacting with the database
   - `db` - database configuration
- `domain` - business logic layer:
   - `model` - classes and enums representing business entities and data models
   - `repository` - abstract repositories providing a clean API for data access from various sources
- `presentation` - presentation layer, reponsible for UI and UI-related logic:
   - `addtask` - fragments handling adding new tasks
   - `home` - main screen fragment and related adapter for displaying task list
   - `notifications` - notifications screen and its adapter
   - `viewmodel` - ViewModel layer managing UI-related data
- `util` - utility and helper classes supporting the app’s functionality:
   - `converter` - data converters for Room database and date types
   - `file` - file-related services, JSON adapters, and XML serialization/deserialization classes
   - `lang` - language support utilities
   - `notication` - helper classes for handling notifications
   - `task` - additional task-related helpers such as base adapter and form helper

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
- Press __Run__ (`Shift + F10` or the green button <img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/green-play-button.png" alt="Green button" title="Green button" height="20"> to launch the app

## Setup 
Functional description
