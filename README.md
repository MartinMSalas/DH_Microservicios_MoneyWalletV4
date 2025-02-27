Digital Money Wallet

¡Bienvenido(a) al repositorio de Digital Money Wallet! Este proyecto consiste en una aplicación de billetera digital desarrollada en Java con Spring Boot, siguiendo un enfoque de microservicios. El objetivo es proporcionar una plataforma escalable, segura y flexible para gestionar usuarios, tarjetas, transacciones y operaciones financieras de manera ágil.
1. Descripción General

Digital Money Wallet ofrece:

    Gestión de usuarios: Registro, autenticación y administración de datos personales.
    Administración de billeteras: Manejo de múltiples billeteras por usuario, con saldos y monedas distintas.
    Tarjetas de pago: Registro y tokenización de tarjetas para mayor seguridad.
    Transacciones: Operaciones de depósito, retiro, transferencia y pago con soporte de múltiples estados (pendiente, completado, fallido).
    Operaciones: Control detallado del flujo de cada transacción (autorización, confirmación, reembolsos, etc.).

El proyecto está diseñado para ser altamente escalable, aprovechando la arquitectura de microservicios y permitiendo la integración con otros servicios de manera modular.
2. Arquitectura de Microservicios

Se ha optado por una arquitectura basada en microservicios para facilitar el desarrollo, despliegue y escalado independiente de cada componente. En líneas generales, el repositorio incluye (o incluirá) los siguientes servicios:

    User Service
        Manejo de la creación, autenticación y actualización de usuarios.
        Exposición de API REST para consultas y modificaciones de perfiles.

    Wallet Service
        Gestión de las billeteras de cada usuario.
        Mantenimiento de saldos y monedas.

    Card Service
        Registro y tokenización de tarjetas de pago.
        Asociación de tarjetas a billeteras y validaciones de seguridad.

    Transaction Service
        Procesamiento de transacciones (depósitos, retiros, pagos, transferencias).
        Manejo de estados y lógica de negocio (autorizaciones, confirmaciones).

    Operation Service
        Registro de las operaciones asociadas a cada transacción.
        Control detallado del flujo de pagos, reembolsos y más.

    API Gateway
        Puerta de entrada unificada para todos los microservicios.
        Enrutamiento y control de acceso.

    Discovery Server
        Servicio (e.g. Eureka, Consul) para el registro automático de los microservicios.
    
    Keycloak Server
        Servicio encargado de la gestion de seguridad

Cada microservicio puede incluir su propia base de datos, permitiendo una mayor independencia y escalabilidad.
3. Tecnologías Principales

    Java 17+: Lenguaje de programación principal.
    Spring Boot 2/3: Framework para simplificar la creación de microservicios.
    Spring Cloud: Opcional, para orquestación de microservicios (Service Discovery, Config Server, Gateway, etc.).
    Maven o Gradle: Para la gestión de dependencias y build.
    Bases de Datos:
        PostgreSQL o MySQL (recomendadas) para persistencia relacional.
        MongoDB (opcional) si se requieren funcionalidades NoSQL.
    Docker / Docker Compose: Para contenedorización y despliegue.
    Swagger/OpenAPI: Para documentar las APIs REST.
