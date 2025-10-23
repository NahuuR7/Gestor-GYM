// Configuración de la base de datos
const DB_NAME = 'SpartacusGYM';
const DB_VERSION = 1;
const STORE_NAME = 'members';

// Elementos del DOM
const userForm = document.getElementById('user-form');
const usersList = document.getElementById('users-list');
const formTitle = document.getElementById('form-title');
const submitBtn = document.getElementById('submit-btn');
const cancelBtn = document.getElementById('cancel-btn');
const filterTurno = document.getElementById('filter-turno');
const filterStatus = document.getElementById('filter-status');

// Estadísticas
const totalUsers = document.getElementById('total-users');
const morningUsers = document.getElementById('morning-users');
const activeUsers = document.getElementById('active-users');

// Variables globales
let editingUserId = null;
let db = null;

// Inicializar la aplicación
document.addEventListener('DOMContentLoaded', function() {
    initDB();
    
    // Establecer fecha mínima para los campos de fecha
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('fecha-inicio').min = today;
    document.getElementById('fecha-fin').min = today;
    
    // Event listeners
    userForm.addEventListener('submit', handleFormSubmit);
    cancelBtn.addEventListener('click', cancelEdit);
    filterTurno.addEventListener('change', renderUsers);
    filterStatus.addEventListener('change', renderUsers);
});

// Inicializar IndexedDB
function initDB() {
    const request = indexedDB.open(DB_NAME, DB_VERSION);

    request.onerror = function(event) {
        console.error('Error al abrir la base de datos:', event.target.error);
        alert('Error al inicializar la base de datos. La aplicación puede no funcionar correctamente.');
    };

    request.onsuccess = function(event) {
        db = event.target.result;
        console.log('Base de datos abierta correctamente');
        renderUsers();
        updateStats();
    };

    request.onupgradeneeded = function(event) {
        const database = event.target.result;
        
        // Crear el object store (similar a una tabla)
        const objectStore = database.createObjectStore(STORE_NAME, { 
            keyPath: 'id', 
            autoIncrement: true 
        });

        // Crear índices para búsquedas eficientes
        objectStore.createIndex('nombre', 'nombre', { unique: false });
        objectStore.createIndex('apellido', 'apellido', { unique: false });
        objectStore.createIndex('turno', 'turno', { unique: false });
        objectStore.createIndex('fechaFin', 'fechaFin', { unique: false });
        
        console.log('Estructura de la base de datos creada');
    };
}

// Manejar envío del formulario
function handleFormSubmit(e) {
    e.preventDefault();
    
    const userData = {
        nombre: document.getElementById('nombre').value.trim(),
        apellido: document.getElementById('apellido').value.trim(),
        edad: parseInt(document.getElementById('edad').value),
        turno: document.getElementById('turno').value,
        fechaInicio: document.getElementById('fecha-inicio').value,
        fechaFin: document.getElementById('fecha-fin').value,
        fechaRegistro: new Date().toISOString()
    };
    
    // Validación adicional
    if (userData.fechaFin <= userData.fechaInicio) {
        alert('La fecha de finalización debe ser posterior a la fecha de inicio');
        return;
    }
    
    if (editingUserId) {
        // Editar usuario existente
        userData.id = editingUserId;
        updateUser(userData);
    } else {
        // Agregar nuevo usuario
        addUser(userData);
    }
}

// Agregar nuevo usuario a la base de datos
function addUser(userData) {
    const transaction = db.transaction([STORE_NAME], 'readwrite');
    const objectStore = transaction.objectStore(STORE_NAME);
    const request = objectStore.add(userData);

    request.onsuccess = function() {
        console.log('Miembro agregado con ID:', request.result);
        userForm.reset();
        renderUsers();
        updateStats();
        showNotification('Miembro agregado correctamente', 'success');
    };

    request.onerror = function() {
        console.error('Error al agregar miembro:', request.error);
        showNotification('Error al agregar miembro', 'error');
    };
}

// Actualizar usuario existente en la base de datos
function updateUser(userData) {
    const transaction = db.transaction([STORE_NAME], 'readwrite');
    const objectStore = transaction.objectStore(STORE_NAME);
    const request = objectStore.put(userData);

    request.onsuccess = function() {
        console.log('Miembro actualizado con ID:', userData.id);
        userForm.reset();
        cancelEdit();
        renderUsers();
        updateStats();
        showNotification('Miembro actualizado correctamente', 'success');
    };

    request.onerror = function() {
        console.error('Error al actualizar miembro:', request.error);
        showNotification('Error al actualizar miembro', 'error');
    };
}

// Eliminar usuario de la base de datos
function deleteUser(userId) {
    if (!confirm('¿Estás seguro de que deseas eliminar este miembro?')) {
        return;
    }

    const transaction = db.transaction([STORE_NAME], 'readwrite');
    const objectStore = transaction.objectStore(STORE_NAME);
    const request = objectStore.delete(userId);

    request.onsuccess = function() {
        console.log('Miembro eliminado con ID:', userId);
        renderUsers();
        updateStats();
        showNotification('Miembro eliminado correctamente', 'success');
    };

    request.onerror = function() {
        console.error('Error al eliminar miembro:', request.error);
        showNotification('Error al eliminar miembro', 'error');
    };
}

// Obtener todos los usuarios de la base de datos
function getAllUsers(callback) {
    const transaction = db.transaction([STORE_NAME], 'readonly');
    const objectStore = transaction.objectStore(STORE_NAME);
    const request = objectStore.getAll();

    request.onsuccess = function() {
        callback(request.result);
    };

    request.onerror = function() {
        console.error('Error al obtener miembros:', request.error);
        callback([]);
    };
}

// Obtener usuario por ID
function getUserById(userId, callback) {
    const transaction = db.transaction([STORE_NAME], 'readonly');
    const objectStore = transaction.objectStore(STORE_NAME);
    const request = objectStore.get(userId);

    request.onsuccess = function() {
        callback(request.result);
    };

    request.onerror = function() {
        console.error('Error al obtener miembro:', request.error);
        callback(null);
    };
}

// Iniciar edición de usuario
function editUser(userId) {
    getUserById(userId, function(user) {
        if (user) {
            document.getElementById('user-id').value = user.id;
            document.getElementById('nombre').value = user.nombre;
            document.getElementById('apellido').value = user.apellido;
            document.getElementById('edad').value = user.edad;
            document.getElementById('turno').value = user.turno;
            document.getElementById('fecha-inicio').value = user.fechaInicio;
            document.getElementById('fecha-fin').value = user.fechaFin;
            
            editingUserId = userId;
            formTitle.textContent = 'Editar Miembro';
            submitBtn.textContent = 'Actualizar Miembro';
            cancelBtn.style.display = 'inline-block';
        }
    });
}

// Cancelar edición
function cancelEdit() {
    editingUserId = null;
    formTitle.textContent = 'Agregar Nuevo Miembro';
    submitBtn.textContent = 'Agregar Miembro';
    cancelBtn.style.display = 'none';
    userForm.reset();
}

// Renderizar lista de usuarios
function renderUsers() {
    if (!db) {
        usersList.innerHTML = '<div class="empty-message">Base de datos no disponible</div>';
        return;
    }

    getAllUsers(function(users) {
        const turnoFilter = filterTurno.value;
        const statusFilter = filterStatus.value;
        
        // Filtrar usuarios
        let filteredUsers = users;
        
        if (turnoFilter) {
            filteredUsers = filteredUsers.filter(user => user.turno === turnoFilter);
        }
        
        if (statusFilter) {
            const today = new Date().toISOString().split('T')[0];
            
            if (statusFilter === 'activo') {
                filteredUsers = filteredUsers.filter(user => user.fechaFin >= today);
            } else if (statusFilter === 'vencido') {
                filteredUsers = filteredUsers.filter(user => user.fechaFin < today);
            }
        }
        
        // Ordenar por fecha de registro (más recientes primero)
        filteredUsers.sort((a, b) => new Date(b.fechaRegistro) - new Date(a.fechaRegistro));
        
        // Mostrar usuarios o mensaje de vacío
        if (filteredUsers.length === 0) {
            usersList.innerHTML = '<div class="empty-message">No se encontraron miembros</div>';
            return;
        }
        
        // Generar HTML para cada usuario
        usersList.innerHTML = filteredUsers.map(user => {
            const today = new Date().toISOString().split('T')[0];
            const isActive = user.fechaFin >= today;
            const statusClass = isActive ? 'user-turn' : 'btn-danger';
            const statusText = isActive ? 'Activo' : 'Vencido';
            
            return `
                <div class="user-card">
                    <div class="user-header">
                        <div class="user-name">${user.nombre} ${user.apellido}</div>
                        <div class="${statusClass}">${statusText}</div>
                    </div>
                    <div class="user-details">
                        <div class="user-detail"><strong>Edad:</strong> ${user.edad} años</div>
                        <div class="user-detail"><strong>Turno:</strong> ${user.turno.charAt(0).toUpperCase() + user.turno.slice(1)}</div>
                        <div class="user-detail"><strong>Inicio:</strong> ${formatDate(user.fechaInicio)}</div>
                        <div class="user-detail"><strong>Fin:</strong> ${formatDate(user.fechaFin)}</div>
                    </div>
                    <div class="actions">
                        <button onclick="editUser(${user.id})">Editar</button>
                        <button class="btn-danger" onclick="deleteUser(${user.id})">Eliminar</button>
                    </div>
                </div>
            `;
        }).join('');
    });
}

// Actualizar estadísticas
function updateStats() {
    if (!db) return;

    getAllUsers(function(users) {
        totalUsers.textContent = users.length;
        
        const morningCount = users.filter(user => user.turno === 'mañana').length;
        morningUsers.textContent = morningCount;
        
        const today = new Date().toISOString().split('T')[0];
        const activeCount = users.filter(user => user.fechaFin >= today).length;
        activeUsers.textContent = activeCount;
    });
}

// Formatear fecha para mostrar
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('es-ES', options);
}

// Mostrar notificaciones
function showNotification(message, type) {
    // Crear elemento de notificación
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 5px;
        color: white;
        font-weight: 600;
        z-index: 1000;
        opacity: 0;
        transform: translateX(100%);
        transition: all 0.3s ease;
        ${type === 'success' ? 'background-color: var(--success);' : 'background-color: var(--accent);'}
    `;

    document.body.appendChild(notification);

    // Animación de entrada
    setTimeout(() => {
        notification.style.opacity = '1';
        notification.style.transform = 'translateX(0)';
    }, 100);

    // Remover después de 3 segundos
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// Función para exportar datos (backup)
function exportData() {
    getAllUsers(function(users) {
        const dataStr = JSON.stringify(users, null, 2);
        const dataBlob = new Blob([dataStr], { type: 'application/json' });
        
        const link = document.createElement('a');
        link.href = URL.createObjectURL(dataBlob);
        link.download = `spartacus_gym_backup_${new Date().toISOString().split('T')[0]}.json`;
        link.click();
    });
}

// Función para importar datos
function importData(event) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function(e) {
        try {
            const users = JSON.parse(e.target.result);
            
            if (confirm(`¿Importar ${users.length} miembros? Esto reemplazará los datos actuales.`)) {
                const transaction = db.transaction([STORE_NAME], 'readwrite');
                const objectStore = transaction.objectStore(STORE_NAME);
                
                // Limpiar base de datos actual
                objectStore.clear();
                
                // Agregar nuevos datos
                users.forEach(user => {
                    objectStore.add(user);
                });
                
                transaction.oncomplete = function() {
                    renderUsers();
                    updateStats();
                    showNotification('Datos importados correctamente', 'success');
                };
            }
        } catch (error) {
            console.error('Error al importar datos:', error);
            showNotification('Error al importar datos', 'error');
        }
    };
    reader.readAsText(file);
}