import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  
  editando: boolean = false;
  filtroTurno: string = '';
  nuevoMiembro: any = { id: null, dni: '', nombre: '', apellido: '', edad: null, turno: '' };
  
  miembros: any[] = []; 
  totalMiembros: number = 0;
  turnoManana: number = 0;
  miembrosActivos: number = 0;

  // Inyectamos ChangeDetectorRef para obligar a Angular a repintar la pantalla
  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  // Volvemos a colocar la carga inicial donde corresponde
  ngOnInit() {
    this.obtenerMiembros();
  }

  // LECTURA Y FILTRO PARA LA LISTA
  obtenerMiembros() {
    let urlBackend = 'http://localhost:8080/api/miembros';
    
    if (this.filtroTurno && this.filtroTurno !== '') {
      urlBackend += '?turno=' + this.filtroTurno;
    }
    
    this.http.get<any[]>(urlBackend).subscribe({
      next: (datos) => {
        this.miembros = datos; 
        // ¡Magia! Despertamos a Angular para que actualice la lista YA MISMO
        this.cdr.detectChanges(); 
      },
      error: (error) => console.error('Error al obtener la lista:', error)
    });

    this.calcularEstadisticas();
  }

  // ESTADÍSTICAS GLOBALES
  calcularEstadisticas() {
    this.http.get<any[]>('http://localhost:8080/api/miembros').subscribe({
      next: (todosLosDatos) => {
        this.totalMiembros = todosLosDatos.length;
        this.turnoManana = todosLosDatos.filter(m => m.turno === 'MANANA').length;
        this.miembrosActivos = todosLosDatos.length; 
        // ¡Magia! Despertamos a Angular para que actualice los números de arriba YA MISMO
        this.cdr.detectChanges(); 
      }
    });
  }

  // CREAR Y ACTUALIZAR
  guardarMiembro() {
    if (this.editando) {
      const urlBackend = 'http://localhost:8080/api/miembros/' + this.nuevoMiembro.id; 
      this.http.put(urlBackend, this.nuevoMiembro).subscribe({
        next: () => {
          alert('¡Miembro actualizado con éxito!');
          this.cancelarEdicion();
          this.obtenerMiembros(); 
        },
        error: (error) => {
          console.error('Detalle del error:', error);
          alert(typeof error.error === 'string' ? error.error : 'Ocurrió un error al actualizar.');
        }
      });
    } else {
      const urlBackend = 'http://localhost:8080/api/miembros'; 
      this.http.post(urlBackend, this.nuevoMiembro).subscribe({
        next: () => {
          alert('¡Miembro guardado con éxito!');
          this.cancelarEdicion();
          this.obtenerMiembros(); 
        },
        error: (error) => {
          console.error('Detalle del error:', error);
          alert(typeof error.error === 'string' ? error.error : 'Ocurrió un error al guardar.');
        }
      });
    }
  }

  // PREPARAR FORMULARIO PARA EDICIÓN
  editarMiembro(miembro: any) {
    this.editando = true;
    this.nuevoMiembro = { ...miembro }; 
    window.scrollTo({ top: 0, behavior: 'smooth' }); 
  }

  // LIMPIAR FORMULARIO
  cancelarEdicion() {
    this.editando = false;
    this.nuevoMiembro = { id: null, dni: '', nombre: '', apellido: '', edad: null, turno: '' };
  }

  // ELIMINAR
  eliminarMiembro(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar a este miembro?')) {
      const urlBackend = 'http://localhost:8080/api/miembros/' + id;
      this.http.delete(urlBackend).subscribe({
        next: () => {
          alert('Miembro eliminado.');
          this.obtenerMiembros();
        },
        error: (error) => alert('Hubo un error al eliminar.')
      });
    }
  }
}