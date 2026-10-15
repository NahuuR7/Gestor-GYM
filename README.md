[README.md](https://github.com/user-attachments/files/30516780/README.md)
# Spartacus GYM

![Version](https://img.shields.io/badge/Versi%C3%B3n-1.0.0-blue)
![Tech](https://img.shields.io/badge/Tecnolog%C3%ADas-HTML5%20%7C%20CSS3%20%7C%20Vanilla%20JS-yellow)
![Database](https://img.shields.io/badge/Base%20de%20Datos-IndexedDB-green)

 📖 Descripción General
El sistema Spartacus GYM es una aplicación web diseñada para facilitar la gestión, el seguimiento y la administración de los miembros de un gimnasio. 
Permite al personal (recepción o administración) registrar, actualizar, eliminar y visualizar rápidamente la información de membresía y su estado de vigencia de forma completamente local en el navegador.

 ✨ Características Principales
* **Gestión de Miembros (CRUD):** Formularios intuitivos para agregar nuevos miembros, editar información existente o eliminar registros, con detalles como nombre, 
edad, turno y periodo de membresía.
* **Panel de Estadísticas en Tiempo Real:** Muestra métricas clave de un vistazo, como el total de miembros registrados, los miembros inscritos específicamente en el turno de la mañana, 
y la cantidad actual de miembros con membresía activa.
* **Listado Filtrable:** Permite buscar y filtrar rápidamente la lista de miembros por su turno asignado (Mañana, Tarde, Noche) y su estado de membresía (Activos o Vencidos).
* **Persistencia de Datos Local:** Utiliza la API IndexedDB del navegador para almacenar toda la información directamente en el dispositivo del usuario, 
sin necesidad de configurar un servidor backend ni depender de una conexión a internet.
* **Respaldo de Datos:** Incluye una herramienta integrada para Exportar e Importar la base de datos local completa mediante archivos en formato JSON.

 🛠️ Tecnologías Utilizadas
* **Frontend:** Tecnologías web estándar, incluyendo HTML5, CSS3 y JavaScript.
* **Base de Datos Local:** API IndexedDB (Base de datos NoSQL integrada en navegadores modernos).
* **Intercambio de Datos:** Formato JSON para la exportación e importación de los backups.

 ⚙️ Requisitos del Sistema
Dado que es una aplicación que se ejecuta íntegramente del lado del cliente, los requisitos de hardware y software son mínimos:
* **Hardware:** Procesador moderno de doble núcleo, 4 GB de memoria RAM o superior, y espacio libre en disco para el sistema operativo y el almacenamiento local.
* **Sistema Operativo:** Compatible con Windows, macOS, Linux, o Android/iOS para su uso en dispositivos móviles.
* **Navegador Web:** Google Chrome (recomendado), Mozilla Firefox, Microsoft Edge, o cualquier navegador moderno actualizado que ofrezca soporte para HTML5, CSS3, JS e IndexedDB.

 🚀 Instalación y Uso
El sistema no requiere un proceso de instalación tradicional ni servidores externos.

1. Clona este repositorio o descarga los archivos del proyecto.
2. Asegúrate de tener los archivos principales (`index.html`, `Style.css` y `Scrip.js`) ubicados en la misma carpeta.
3. Para ejecutar la aplicación, simplemente abre el archivo `index.html` con tu navegador web preferido (doble clic).
4. En su primera ejecución, el sistema inicializará automáticamente la base de datos `SpartacusGYM` y estará listo para su uso.

🗄️ Estructura de Datos (IndexedDB)
Los registros de cada miembro se almacenan en el *Object Store* con el siguiente esquema JSON:
```json
{
  "id": "(autoIncrement)",
  "nombre": "string",
  "apellido": "string",
  "edad": "number",
  "turno": "string ('mañana'/'tarde'/'noche')",
  "fechaInicio": "YYYY-MM-DD",
  "fechaFin": "YYYY-MM-DD",
  "fechaRegistro": "ISOString"
}
```

 👥 Equipo de Desarrollo y Créditos
Desarrollado como Proyecto Final para la materia Modelado de Software - Segundo Año (Septiembre 2025).
Integrantes del Equipo: Rivera Nahuel - Reynaga Eduardo - Vargas Emiliano.
