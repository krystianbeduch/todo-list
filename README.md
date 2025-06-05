# To-Do List
The project was developed as part of my IT studies during a course on mobile application design.

The application is a task planner designed to help you organize your daily activities. You can assign priorities to tasks, set deadlines, mark tasks as completed, and attach files. A notification mechanism reminds you of upcoming deadlines and alerts you to tasks you have not completed on time. Application designed based on Model-View-ViewModel (MVVM) design pattern.

### Contents
1. [Application functionality](#application-functionality)
2. [Technology](#technology)
3. [Project structure](#project-structure)
4. [Database schema](#database-schema)
5. [Setup](#setup)
6. [Functional description](#functional-description)
   - [List of tasks](#list-of-tasks)
   - [Add new task](#add-new-task)
   - [Managing tasks](#managing-tasks)
   - [Edit task](#edit-task)
   - [Managing attachments](#managing-attachments)
   - [Notification mechanism](#notification-mechanism)
   - [Import and export of task list to files](#import-and-export-of-task-list-to-files)
   - [Changing the application language](#changing-the-application-language)

## Application functionality
- Displays all saved tasks in a clear list, allowing easy browsing and access to task details
- Users can quickly add a new task by specifying its title, priority and deadline
- Task management - marking tasks as completed, deleting tasks or sorting tasks based on date added, title, priority, task status or deadline
- List tasks can be edited to update their titles, deadlines or priorities at any time
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
### Java
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

### Resource directory (`res`)
The `res` folder contains all app resources organized into subdirectories by type:
- `drawable` - image assets and icons in PNG and XML formats, including app icons, navigation icons, and language flags
- `layout` - XML layouts for activities, fragments, and list items, defining the UI structure
- `menu` - XML files defining menus such as bottom navigation, sorting, and task context menus
- `mipmap-*` - launcher icons in various resolutions (mdpi, hdpi, xhdpi, etc.) optimized for different screen densities
- `navigation` - navigation graph XML describing app navigation flow
- `values` — XML files holding colors, dimensions, strings, and themes used throughout the app
- `values-en` - english string resources
- `values-night` - resources for night mode themes
- `xml` - miscellaneous XML configurations, including backup rules, file paths, and data extraction settings

## Database schema
<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/database-schema.png" alt="Database schema" title="Database schema" height="350">

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
- Press __Run__ (`Shift + F10` or the green button <img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/green-play-button.png" alt="Green button" title="Green button" height="20">) to launch the app

## Functional description
### List of tasks
Presents all tasks in a scrollable list using a _RecyclerView_. Each task is visually styled according to the `item_task.xml` layout, ensuring a consistent and user-friendly appearance. 
Each task item displays the following information:
- Title — the main name or description of the task
- Deadline — the due date and time by which the task should be completed
- Creation date — the date and time when the task was originally added
- Priority — indicates the importance level of the task, which can be _High_, _Medium_, or _Low_
- Completion status — represented by a ✔ symbol when the task is marked as completed
- Attachment icon — displayed if one or more attachments are associated with the task, indicating additional files or resources linked to it

<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/list-of-tasks.jpg" alt="List of tasks" title="List of tasks" height="800">

This setup provides a clear and informative overview of all tasks, helping users to easily track, prioritize, and manage their work

### Add new task
To add a new task, the user navigates to a dedicated fragment accessible from the bottom navigation menu. This fragment presents a form where the user can enter all necessary details for the task creation. 
The form includes the following fields:
- Task title — a text input where the user specifies the title of the task.
- Deadline — a date and time picker allowing the user to set the task’s deadline.
- Priority — a dropdown menu from which the user selects the task’s priority level (_High_, _Medium_, _Low_).

<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/add-new-task.jpg" alt="Add new task" title="Add new task" height="800">

The form is designed with validation rules to ensure data integrity:
- Empty fields are not accepted, preventing the creation of tasks without essential information.
- Due date cannot be set to a past date, ensuring that deadlines are always in the present or future.

<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/add-new-task-empty-fields.jpg" alt="Add new task - empty fields" title="Add new task - empty fields" height="800">
<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/add-new-task-past-date.jpg" alt="Add new task - past date" title="Add new task - past date" height="800">

### Managing tasks
The application provides a flexible and intuitive interface for managing tasks. 
- Interacting with a task - tapping on a task in the list opens a context menu with several options:
   - [Edit Task](#edit-task) - allows the user to update the task details
   - Delete Task – permanently removes the task
   - Mark as Done / Undone – toggles the task completion status
   - [Managing attachments](#managing-attachments)
- Quick Status Change - long-pressing a task directly toggles its completion status (✔)
- Task Sorting - tasks can be sorted dynamically by the user using a _spinner_ menu located in the toolbar. The available sorting options include:
   - Creation Date (default)
   - Title
   - Deadline
   - Priority
   - Status <br>
<p align="center">
   <img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/managing-tasks-menu.jpg" alt="Managing tasks menu" title="Managing tasks menu" height="800">
   <img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/managing-tasks-sort-by-priority.jpg" alt="Managing tasks - sort by priority" title="Managing tasks - sort by priority" height="800"> 
</p>

### Edit Task
Selecting the `Edit` option from a task’s context menu navigates the user to a dedicated activity for editing. 
The interface presents a form identical to the one used for adding a new task. However, the form fields are pre-filled with the existing task data.
The user can update any of these values and save the changes. The form retains the same validation mechanisms.

<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/edit-task.jpg" alt="Edit task" title="edit-task" height="800">

### Managing attachments
Attachments can be managed through a task’s context menu. The following operations are available:
- Add attachment:
   - User can attach files (image, video, PDF document) to a task directly from their device. When a file is selected, it is copied to the application's internal storage, ensuring the attachment remains accessible even if the original file is deleted from the device.
- View attachments:
   - User can view all attachments linked to a specific task. Each file is opened using an appropriate external application, if available on the device.
- Delete attachment:
   - User can delete individual attachments from a task. This action removes the file from both the task and the application's internal storage.
 
<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/managing-attachments.jpg" alt="Managing attachments" title="Managing attachments" height="800">

### Notification mechanism
The app implements a notification system to help users stay on top of their tasks’ deadlines:
- When the application starts, it automatically shows notifications for all incomplete tasks whose deadlines are within the next 24 hours or have already passed. This ensures users are immediately notified of upcoming or overdue tasks
- Accessible via the bottom navigation menu, the `Notifications` fragment displays the total count of tasks with active notifications. Inside this fragment, users can see detailed information about each task along with the notification type:
   - __"Task deadline has passed"__ for overdue tasks
   - __"Upcoming task deadline"__ for tasks whose deadline is approaching within 24 hours
  
<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/notification-mechanism.jpg" alt="Notification mechanism" title="Notification mechanism" height="800">
<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/notification-fragment.jpg" alt="Notification fragment" title="Notification fragment" height="800">

### Import and export of task list to files
The application provides the ability to import and export the task list to external files, allowing users to back up their tasks or load them from other sources.
This functionality is accessible through the `More` section in the bottom navigation menu, where two options are available: `Import tasks` and `Export tasks`:
- Exporting tasks
   - After selecting the `Export` option, a dialog window appears where the user can choose the file format. Once the export process is completed successfully, a confirmation message is shown to the user. The exported file is saved to the device's `Downloads` directory.
- Importing tasks
   - After selecting the `Import` option, the app opens a file picker, allowing the user to select a file from the device's storage. A built-in mechanism checks the file extension to ensure it matches the expected formats. Files with unsupported extensions are automatically rejected, and an error message is displayed. It is important that the structure of the imported file adheres to the required format. If the format is incorrect or inconsistent, the import will fail, and an appropriate error message will be shown to the user. The `id` field at the import stage is ommited although it is required in the file.
 
#### CSV
```csv
ID;Title;Deadline;Priority;Status;Created at
1;Example task 1;08.06.2025 12:00;HIGH;true;04.06.2025 23:56
2;Example task 2;30.06.2025 14:30;MEDIUM;false;02.06.2025 16:56
```

#### JSON
```json
[
   {
      "id": 1,
      "title": "Example task 1",
      "deadline": "08.06.2025 12:00",
      "priority": "HIGH",
      "isDone": true,
      "createdAt": "04.06.2025 23:56"
   },
   {
      "id": 2,
      "title": "Example task 2",
      "deadline": "30.06.2025 14:30",
      "priority": "MEDIUM",
      "isDone": false,
      "createdAt": "02.06.2025 16:56"
    }
]
```

#### XML
```XML
<Tasks>
   <Task>
      <id>1</id>
      <title>Example task 1</title>
      <deadline>08.06.2025 12:00</deadline>
      <priority>HIGH</priority>
      <isDone>true</isDone>
      <createdAt>04.06.2025 23:56</createdAt>      
   </Task>
   <Task>
      <id>2</id>
      <title>Example task 2</title>
      <deadline>30.06.2025 14:30</deadline>
      <priority>MEDIUM</priority>
      <isDone>false</isDone>
      <createdAt>02.06.2025 16:56</createdAt>
   </Task>
</Tasks>
```

<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/import-tasks.jpg" alt="Import tasks" title="Import tasks" height="800">

### Changing the application language
The application allow users to switch between available languages: Polish and English. There are two ways to change the language:
- Through the `More` Section - in the bottom navigation menu, under the `More` tab, users can select the preferred language from the available options. Selecting a language immediately updates the app's interface to reflect the new setting.
- Language icon on the toolbar - the `Home` screen _Toolbar_ displays a flag icon representing the currently selected language. Tapping on this icon also allows the user to toggle between languages quickly and intuitively.

<img src="https://github.com/krystianbeduch/todo-list/blob/main/readme-images/changing-app-language-to-polish.jpg" alt="Changing app language to polish" title="Changing app language to polish" height="800">

