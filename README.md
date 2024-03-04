# MCServerLoader
Sistema de arranque y gestión automática de [servidores de Minecraft](https://www.minecraft.net/es-es/download/server). El programa permite gestionar el inicio y apagado automático del servidor en base a las peticiones de los usuarios para un mayor ahorro de energia y recursos

## 🛠️ Configuración del cargador
El sistema de arranque se puede configurar desde el fichero "mcloader.conf" con la sintaxis ```parametro=valor```
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

## 📋 Requisitos del sistema
Los siguientes requisitos son necesarios para poder ejecutar el programa
- SO Linux
- JRE 11 o superior
- 1GB RAM minimo
