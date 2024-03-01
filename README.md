# MCServerLoader
Sistema de arranque y gestión automática de servidores de Minecraft. El programa permite gestionar el inicio y apagado automático del servidor en base a las peticiones de los usuarios para un mayor ahorro de energia y recursos

## 🛠️ Configuración del cargador

| Key   | Significado |
|-------|-------|
| javaargs | Parámetros de arranque de java para Minecraft |
| port | Puerto de funcionamiento del servidor |
| mc-jar | Path del fichero jar del servidor de minecraft |
| log-pointer | Apuntador del sistema en los registros del juego |
| start-time | Hora de apertura del servidor |
| end-time | Hora de cierre del servidor |
| timeout | Tiempo de espera para preparar el socket a escucha de peticiones |
| verification-timeout | Tiempo de espera entre verificaciones de usuario y horarios |
