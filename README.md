<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Tasks</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            width: 100%;
            max-width: 400px;
            padding: 20px;
        }

        .tasks-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }

        .tasks-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
        }

        .tasks-header h1 {
            font-size: 28px;
            font-weight: 600;
            margin-bottom: 5px;
        }

        .tasks-header p {
            opacity: 0.9;
            font-size: 14px;
        }

        .tasks-list {
            padding: 20px;
        }

        .task-item {
            display: flex;
            align-items: center;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 12px;
            margin-bottom: 10px;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .task-item:hover {
            background: #e9ecef;
            transform: translateY(-2px);
        }

        .task-checkbox {
            width: 22px;
            height: 22px;
            border: 2px solid #667eea;
            border-radius: 6px;
            margin-right: 15px;
            background: white;
            transition: all 0.2s;
        }

        .task-checkbox.checked {
            background: #667eea;
            border-color: #667eea;
            position: relative;
        }

        .task-checkbox.checked::after {
            content: '✓';
            color: white;
            position: absolute;
            font-size: 16px;
            transform: translate(-50%, -50%);
        }

        .task-content {
            flex: 1;
        }

        .task-title {
            font-weight: 500;
            color: #2d3748;
            margin-bottom: 4px;
        }

        .task-title.completed {
            text-decoration: line-through;
            color: #a0aec0;
        }

        .task-date {
            font-size: 12px;
            color: #718096;
        }

        .add-task-section {
            padding: 20px;
            border-top: 2px solid #e2e8f0;
        }

        .add-task-btn {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        .add-task-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .modal.active {
            display: flex;
        }

        .modal-content {
            background: white;
            border-radius: 20px;
            width: 90%;
            max-width: 350px;
            padding: 25px;
            animation: slideUp 0.3s ease;
        }

        @keyframes slideUp {
            from {
                transform: translateY(50px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .modal-content h2 {
            color: #2d3748;
            margin-bottom: 20px;
            font-size: 20px;
        }

        .modal-content input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e2e8f0;
            border-radius: 10px;
            font-size: 16px;
            margin-bottom: 20px;
            outline: none;
            transition: border-color 0.2s;
        }

        .modal-content input:focus {
            border-color: #667eea;
        }

        .modal-actions {
            display: flex;
            gap: 10px;
        }

        .modal-actions button {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
        }

        .cancel-btn {
            background: #e2e8f0;
            color: #4a5568;
        }

        .cancel-btn:hover {
            background: #cbd5e0;
        }

        .save-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .save-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .empty-state {
            text-align: center;
            padding: 30px;
            color: #a0aec0;
        }

        .empty-state svg {
            width: 80px;
            height: 80px;
            margin-bottom: 15px;
            opacity: 0.5;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="tasks-card">
            <div class="tasks-header">
                <h1>My Tasks</h1>
                <p id="taskCount">3 tasks remaining</p>
            </div>
            
            <div class="tasks-list" id="tasksList">
                <!-- Tasks will be dynamically added here -->
            </div>

            <div class="add-task-section">
                <button class="add-task-btn" onclick="openModal()">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M12 5v14M5 12h14"/>
                    </svg>
                    Add New Task
                </button>
            </div>
        </div>
    </div>

    <!-- Add Task Modal -->
    <div class="modal" id="taskModal">
        <div class="modal-content">
            <h2>Add New Task</h2>
            <input type="text" id="taskInput" placeholder="Enter task name" value="dasd">
            <div class="modal-actions">
                <button class="cancel-btn" onclick="closeModal()">Cancel</button>
                <button class="save-btn" onclick="saveTask()">Save</button>
            </div>
        </div>
    </div>

    <script>
        let tasks = [
            { id: 1, title: 'ADS', completed: false, date: 'Today' }
        ];

        function renderTasks() {
            const tasksList = document.getElementById('tasksList');
            const taskCount = document.getElementById('taskCount');
            
            if (tasks.length === 0) {
                tasksList.innerHTML = `
                    <div class="empty-state">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
                            <path d="M12 2a10 10 0 0 1 10 10 10 10 0 0 1-10 10 10 10 0 0 1-10-10 10 10 0 0 1 10-10z M8 12h8 M12 8v8"/>
                        </svg>
                        <p>No tasks yet. Add one!</p>
                    </div>
                `;
                taskCount.textContent = '0 tasks';
                return;
            }

            const incompleteTasks = tasks.filter(task => !task.completed).length;
            taskCount.textContent = `${incompleteTasks} task${incompleteTasks !== 1 ? 's' : ''} remaining`;

            tasksList.innerHTML = tasks.map(task => `
                <div class="task-item" onclick="toggleTask(${task.id})">
                    <div class="task-checkbox ${task.completed ? 'checked' : ''}"></div>
                    <div class="task-content">
                        <div class="task-title ${task.completed ? 'completed' : ''}">${task.title}</div>
                        <div class="task-date">${task.date || 'Today'}</div>
                    </div>
                </div>
            `).join('');
        }

        function openModal() {
            document.getElementById('taskModal').classList.add('active');
            document.getElementById('taskInput').value = '';
            document.getElementById('taskInput').focus();
        }

        function closeModal() {
            document.getElementById('taskModal').classList.remove('active');
        }

        function saveTask() {
            const taskInput = document.getElementById('taskInput');
            const taskTitle = taskInput.value.trim();
            
            if (taskTitle) {
                const newTask = {
                    id: Date.now(),
                    title: taskTitle,
                    completed: false,
                    date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric' })
                };
                
                tasks.push(newTask);
                renderTasks();
                closeModal();
            }
        }

        function toggleTask(taskId) {
            tasks = tasks.map(task => 
                task.id === taskId ? { ...task, completed: !task.completed } : task
            );
            renderTasks();
        }

        // Close modal when clicking outside
        window.onclick = function(event) {
            const modal = document.getElementById('taskModal');
            if (event.target === modal) {
                closeModal();
            }
        }

        // Handle Enter key
        document.getElementById('taskInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                saveTask();
            }
        });

        // Initial render
        renderTasks();
    </script>
</body>
</html>
