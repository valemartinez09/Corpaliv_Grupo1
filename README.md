# CORPALIV – App móvil de difusión de impacto social y vinculación empresarial

Proyecto de la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles** (Duoc UC), Evaluación Parcial 2 (encargo en parejas).

Aplicación Android desarrollada con **Kotlin** y **Jetpack Compose** que representa una solución para la **Corporación de Padres y Amigos por el Limitado Visual (CORPALIV) y su Red Sociolaboral Alba Lab**.

> ⚠️ **Proyecto académico (MVP).** Todos los datos son ficticios. No ingreses datos personales reales en la app.

---

## 👥 Integrantes

Valentina Martinez y Alexandra Vicuña

**Equipo:** Grupo 1 · **Sección:** [DSY1105] ·

## 🔗 Enlaces

- Repositorio: [URL DEL REPOSITORIO EN GITHUB]

---

## 🎯 Contexto y problema

CORPALIV promueve la autonomía, la participación y la inclusión social y laboral de personas con discapacidad múltiple, a través de la Escuela Especial Jan Van Dijk y la Red Sociolaboral Alba Lab.

Hoy la información sobre cómo colaborar está dispersa en reuniones, redes sociales y contacto directo. Esto dificulta que el interés inicial de una persona o empresa se transforme en una colaboración concreta (donación, alianza, voluntariado, compra de productos o capacitación).

**Solución:** una app móvil que centraliza la propuesta de valor de la organización, explica de forma simple cómo colaborar y permite dejar un registro de interés que el equipo de vinculación puede revisar.

## ✨ Funcionalidades implementadas

### Vista de usuario (personas y empresas)

- **Pantalla de inicio** con logo, identidad visual de CORPALIV y accesos a las secciones principales.
- **Quiénes somos:** descripción de la organización y de cómo ayudar.
- **Nuestros programas:** inclusión sociolaboral, producción y comercialización, y Escuela Jan Van Dijk.
- **Quiero colaborar:** el usuario elige colaborar como **persona** o como **empresa**.
- **Tu aporte (personas):** elección entre donación única, hacerse socio (donación mensual) u otra forma de ayudar.
- **Formulario de interés** (adaptado según sea persona o empresa):
  - Campos con ícono y mensaje de error por campo.
  - Validaciones: nombre obligatorio, persona de contacto obligatoria para empresas, correo con formato válido, tipo de colaboración y aceptación de contacto obligatorios.
  - Carta de presentación opcional para voluntariado (PDF, Word o texto, máximo 5 MB).
  - Indicador de carga durante el envío.
- **Resumen** con los datos ingresados y mensaje de agradecimiento ("nos pondremos en contacto a la brevedad").

### Vista de funcionario (equipo de vinculación)

- Acceso desde un ícono en la pantalla de inicio.
- **Login** con validación de campos y retroalimentación visual.
- **Panel** con la lista de registros de interés recibidos (los más recientes primero), con opción de **eliminar** con confirmación y cierre de sesión.

### Credenciales de demostración (funcionario)

| Usuario | Contraseña |
|---|---|
| `funcionario` | `alba2026` |

> Son credenciales ficticias solo para el MVP académico. El login se valida de forma local, sin backend.

## 🧩 Requisitos de la evaluación y dónde se cumplen

| Requisito | Implementación |
|---|---|
| Interfaz organizada y navegación clara | Material Design 3, tema propio con la paleta del logo, `NavHost` con rutas centralizadas (`navigation/`) |
| Formularios validados con íconos y mensajes visuales | `CampoTexto` reutilizable (ícono inicial, ícono de advertencia y texto de error), formulario de interés y login |
| Validaciones desde la lógica | Se ejecutan en `FormularioViewModel` y `LoginViewModel`; la UI solo muestra el estado |
| Animaciones funcionales | Transiciones entre pantallas, `Crossfade` del formulario al resumen, `AnimatedVisibility` (tarjetas y carta), animación de pulso en el inicio e indicadores de carga |
| Estructura modular y persistencia local | MVVM + repositorio + **Room** (SQLite) para los registros de interés |
| Recursos nativos | Selector de documentos del sistema (`OpenDocument`) para adjuntar la carta de presentación |
| GitHub + Trello | Ver sección *Colaboración* |

## 🏗️ Arquitectura

Patrón **MVVM** con separación por capas:

```
app/src/main/java/com/app/corpaliv_grupo1/
├── data/          # Room: RegistroEntity, RegistroDao, AppDatabase
├── model/         # Modelos y estados de UI (Registro, FormularioUiState, enums de colaboración)
├── repository/    # RegistroRepository (único punto de acceso a la base de datos)
├── viewmodel/     # FormularioViewModel, LoginViewModel, PanelFuncionarioViewModel
├── navigation/    # Rutas y NavHost (AppNavigation, Routes)
├── ui/
│   ├── screens/   # Pantallas Compose
│   ├── components/# Componentes reutilizables (CampoTexto)
│   └── theme/     # Colores, tipografía y tema (Corpaliv_Grupo1Theme)
└── util/          # Archivos: lectura y validación del documento adjunto
```

**Flujo de datos:** `Pantalla (Compose)` → `ViewModel (StateFlow)` → `Repository` → `DAO (Room)`. El panel del funcionario observa un `Flow` de Room, por lo que se actualiza solo cuando llega o se elimina un registro.

## 🛠️ Tecnologías

- Kotlin 2.2.10 y Jetpack Compose (BOM 2026.02.01) con Material 3
- Navigation Compose 2.7.7
- Lifecycle ViewModel / StateFlow
- Room 3.0.3 (`androidx.room3`) con KSP 2.3.6 y `AndroidSQLiteDriver`
- Android Gradle Plugin 9.4.1
- `minSdk` 24 · `compileSdk` / `targetSdk` 37

## ▶️ Cómo ejecutar el proyecto

**Requisitos**

- Android Studio reciente y compatible con Android Gradle Plugin 9.4.1
- Android SDK 37 instalado (desde *SDK Manager*)
- Emulador o dispositivo con Android 7.0 (API 24) o superior

**Pasos**

1. Clona el repositorio:
   ```bash
   git clone [URL DEL REPOSITORIO EN GITHUB]
   ```
2. Abre la carpeta del proyecto en Android Studio (*File > Open*).
3. Espera a que termine la sincronización de Gradle (*Sync Project with Gradle Files*).
4. Selecciona un emulador o conecta un dispositivo con la depuración USB activada.
5. Presiona **Run ▶** (módulo `app`).

**Cómo probar el flujo completo**

1. En el inicio, toca **Quiero colaborar** y elige *persona* o *empresa*.
2. Completa el formulario con datos ficticios y envía (prueba también dejar campos vacíos para ver las validaciones).
3. Vuelve al inicio, toca el ícono de acceso para funcionarios e ingresa con `funcionario` / `alba2026`.
4. En el panel verás el registro que acabas de enviar.

## 🤝 Colaboración y control de versiones

- **Git/GitHub:** trabajo por ramas (`feature/...`) integradas a `main`, con commits descriptivos de cada cambio.

## ⚠️ Alcance y limitaciones (MVP)

- No existe backend: los registros se guardan solo en el dispositivo, con Room.
- El login del funcionario es una simulación local con credenciales ficticias.
- Solo se adjunta el nombre y los datos del documento en el formulario; el archivo no se envía a ningún servidor.
- Versión pensada para Android; no incluye versión iOS.

## 📄 Licencia y uso académico

Proyecto desarrollado con fines académicos para Duoc UC. Los datos y textos son de ejemplo.
