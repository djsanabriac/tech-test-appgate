# Prueba técnica AppGate

La solución se plantea como un servidor en springboot, en su implementación básica que genera un jar ejecutable con un Tomcat embebido.

Al usar docker se pueden generar diferentes instancias a partir de la misma imágen, con esto se puede utilizar un servicio como ECR para almacenar las imágenes. A través de Fargate se podría configurar de manera que, usando un balanceador y configuración de autoescalamiento, se puedan crear nuevas instancias para soportar el evental tráfico.

Se maneja el almacenamiento de las sesiones y los pasos en BD para que sea independiente de la instancia a la cual se esté apuntando.