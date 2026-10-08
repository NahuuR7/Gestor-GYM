// Elementos del DOM
const userForm = document.getElementById('user-form');
const usersList = document.getElementById('users-list');
const formTitle = document.getElementById('form-title');
const submitBtn = document.getElementById('submit-btn');
const cancelBtn = document.getElementById('cancel-btn');
const filterTurno = document.getElementById('filter-turno');

// Estadísticas
const totalUsers = document.getElementById('total-users');
const morningUsers = document.getElementById('morning-users');
const activeUsers = document.getElementById('active-users');

// Variables globales
let editingUserId = null;
const API_URL = 'http://localhost:8080/api/miembros';

// Inicializar la aplicación
document.addEventListener('DOMContentLoaded', function() {
    // Actualizar año del footer
    document.getElementById('footer-text').innerHTML = `Gestor de Turnos para Gimnasio &copy; ${new Date().getFullYear()}`;
    
    // Cargar datos iniciales
    renderUsers();
    
    // Event listeners
    userForm.addEventListener('submit', handleFormSubmit);
    cancelBtn.addEventListener('click', cancelEdit);
    filterTurno.addEventListener('change', renderUsers);
});

// Manejar envío del formulario
function handleFormSubmit(e) {
    e.preventDefault();
    
    const turnoSeleccionado = document.getElementById('turno').value;
    let turnoAPI = turnoSeleccionado.toUpperCase();
    if(turnoAPI === 'MAÑANA') turnoAPI = 'MANANA'; // Adaptación para el Enum de Java

    const userData = {
        dni: document.getElementById('dni').value.trim(),
        nombre: document.getElementById('nombre').value.trim(),
        apellido: document.getElementById('apellido').value.trim(),
        edad: parseInt(document.getElementById('edad').value),
        turno: turnoAPI
    };
    
    if (editingUserId) {
        // La actualización completa requiere el endpoint PUT, que implementaremos más adelante.
        showNotification('La edición completa requiere la vista detallada', 'warning');
        cancelEdit();
    } else {
        addUser(userData);
    }
}

// Agregar nuevo usuario vía API
function addUser(userData) {
    fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text) });
        }
        return response.json();
    })
    .then(data => {
        console.log('Miembro agregado en la DB con ID:', data.id);
        userForm.reset();
        renderUsers();
        showNotification('Miembro agregado correctamente', 'success');
    })
    .catch(error => {
        console.error('Error al agregar miembro:', error);
        showNotification(error.message || 'Error al conectar con el servidor', 'error');
    });
}

// Obtener todos los usuarios desde la API
function getAllUsers(callback) {
    fetch(API_URL)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error de red');
            }
            return response.json();
        })
        .then(users => {
            // Adaptar los datos del backend para la vista
            const formattedUsers = users.map(user => {
                let turnoVista = user.turno.toLowerCase();
                if (turnoVista === 'manana') turnoVista = 'mañana';
                
                return {
                    ...user,
                    turno: turnoVista
                };
            });
            callback(formattedUsers);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            callback([]);
        });
}

// Renderizar lista de usuarios
function renderUsers() {
    getAllUsers(function(users) {
        const turnoFilter = filterTurno.value;
        
        // Filtrar usuarios
        let filteredUsers = users;
        if (turnoFilter) {
            filteredUsers = filteredUsers.filter(user => user.turno === turnoFilter);
        }
        
        // Actualizar estadísticas basadas en todos los usuarios obtenidos
        updateStats(users);
        
        // Mostrar usuarios o mensaje de vacío
        if (filteredUsers.length === 0) {
            usersList.innerHTML = '<div class="empty-message">No se encontraron miembros en la Base de Datos</div>';
            return;
        }
        
        // Generar HTML
        usersList.innerHTML = filteredUsers.map(user => {
            // En esta etapa del MVP consideramos a todos inactivos hasta que paguen
            const statusClass = 'user-status-danger';
            const statusText = 'Sin Pago Registrado';
            
            return `
                <div class="user-card vencido">
                    <div class="user-header">
                        <div class="user-name">${user.nombre} ${user.apellido}</div>
                        <div class="${statusClass}">${statusText}</div>
                    </div>
                    <div class="user-details">
                        <div class="user-detail"><strong>DNI:</strong> ${user.dni}</div>
                        <div class="user-detail"><strong>Edad:</strong> ${user.edad} años</div>
                        <div class="user-detail"><strong>Turno:</strong> ${user.turno.charAt(0).toUpperCase() + user.turno.slice(1)}</div>
                        <div class="user-detail"><strong>Registrado:</strong> ${formatDate(user.fechaRegistro)}</div>
                    </div>
                    <div class="actions">
                        <button class="btn-success" onclick="registrarPago(${user.id})">Registrar Pago</button>
                    </div>
                </div>
            `;
        }).join('');
    });
}

// Actualizar estadísticas con datos del servidor
function updateStats(users) {
    totalUsers.textContent = users.length;
    
    const morningCount = users.filter(user => user.turno === 'mañana').length;
    morningUsers.textContent = morningCount;
    
    // Hasta que conectemos los pagos, mostraremos 0
    activeUsers.textContent = 0;
}

// Formatear fecha para mostrar
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('es-ES', options);
}

// Funciones placeholder
function cancelEdit() {
    editingUserId = null;
    formTitle.textContent = 'Agregar Nuevo Miembro';
    submitBtn.textContent = 'Agregar Miembro';
    cancelBtn.style.display = 'none';
    userForm.reset();
}

function registrarPago(id) {
    showNotification('Funcionalidad de pagos en desarrollo para la próxima iteración', 'success');
}

// Mostrar notificaciones
function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed; top: 20px; right: 20px; padding: 15px 20px;
        border-radius: 5px; color: white; font-weight: 600; z-index: 1000;
        opacity: 0; transform: translateX(100%); transition: all 0.3s ease;
        ${type === 'success' ? 'background-color: var(--success);' : type === 'warning' ? 'background-color: var(--warning);' : 'background-color: var(--accent);'}
    `;
    document.body.appendChild(notification);
    setTimeout(() => { notification.style.opacity = '1'; notification.style.transform = 'translateX(0)'; }, 100);
    setTimeout(() => {
        notification.style.opacity = '0'; notification.style.transform = 'translateX(100%)';
        setTimeout(() => { if (notification.parentNode) notification.parentNode.removeChild(notification); }, 300);
    }, 3000);
}