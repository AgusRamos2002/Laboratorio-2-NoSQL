#
# **Tarea 2**

## Taller de Bases de Datos NoSQL

## Agustín Ramos

## Damian Dalto

## Sebastian Miranda

# **Índice:**

**[Instalación y Configuración](#_1gg1g2q4oc5u) 3**

**[Formato de intercambio de datos](#_o2fjyq5ce12h) 5**

**[Descripción URL de los Servicios](#_1hicwdh2nbgl) 6**

[Obligatorias](#_1aaf8zc7jwq8) 6

[Método GET](#_bxex2138n6re) 6

[Método POST](#_xqbhdms280t) 6

[Método PUT](#_s70t9vm2e6hh) 7

[Extras](#_tmz6rcczqvnv) 8

[Método Delete](#_2vph24nnhcvr) 8

**[IDE, Lenguaje de Programación y Base de Datos que se utilizaron](#_snrpbboxewmp) 9**

## **Instalación y Configuración**

![](RackMultipart20221107-1-i9dw1y_html_b1253b1d1c0a9530.png) ![](RackMultipart20221107-1-i9dw1y_html_e6c746176f89b5b7.png) ![](RackMultipart20221107-1-i9dw1y_html_401f5fe076bc5317.png)

Para la correcta ejecución de este obligatorio se necesita tres programas esenciales:

- **Postman** , el programa que nos permite utilizar las distintas operaciones a la aplicación detalladas más abajo.
- **IntelliJ IDEA** , el IDE utilizado para crear el Backend de la aplicación, y con el cual la ejecutaremos.
- **MongoDB** , la base de datos que se utiliza para almacenar los diferentes documentos que se comunican con el IDE.

La instalación del obligatorio es sencilla, para ello seguiremos una serie de pasos que se explicaran a continuación:

1. Ir al link del repositorio del proyecto ([https://github.com/AgusRamos2002/Laboratorio-2-NoSQL](https://github.com/AgusRamos2002/Laboratorio-2-NoSQL))
2. Clonarlo o descargarlo como archivo .zip. En caso de haberlo descargado como .zip, se debe extraer. En caso de clonarlo se debe abrir la consola de Git (Gitbash) y escribir el siguiente comando: 'git clone https://github.com/AgusRamos2002/Laboratorio-2-NoSQL.git'
3. Abrir la carpeta que contenga el proyecto con el IDE (en este caso nosotros usamos Intellij Idea) y esperar a que el programa termine de bajar las dependencias.
4. Correr en una consola de comandos el comando 'mongod' para inicializar el servicio de la BD.
5. Ejecutar el proyecto en el IDE.
6. Abrir Postman para probar los servicios con el dominio http://localhost:8080.

![](RackMultipart20221107-1-i9dw1y_html_69a51610b0acd1fd.png)

En cuanto a la configuración, no hay mucho que destacar, ya que como se explicó anteriormente el IDE utilizado en este caso se encarga de descargar las dependencias necesarias para la aplicación.

El único archivo que tiene algo de peso en esta sección es el **application.yml** , el cual establece la URL y el nombre de la Base de Datos.

![](RackMultipart20221107-1-i9dw1y_html_4cb194e070c9cd1.png)

##


## **Formato de intercambio de datos**

Para el presente obligatorio, el intercambio de datos entre el Backend y la Base de Datos se hace a través del uso de JSON, un formato ligero de intercambio de datos el cual es sencillo de leer y escribir (para el programador), y fácil de interpretar y de generar (para una máquina), siendo así un formato muy versátil para este apartado.

Cabe destacar que la utilización de este formato es un requisito obligatorio no funcional.

![](RackMultipart20221107-1-i9dw1y_html_ffd8d5efbb7858bd.png)

## **Descripción URL de los Servicios**

La URL raíz que se utiliza para poder acceder a todas las funcionalidades implementadas en el código es la siguiente: **http://localhost:8080/usuarios****.**

A continuación se distinguen dos categorías para las funcionalidades: las obligatorias (pedidas expresamente en la letra del obligatorio) y las extras (hechas por decisión del grupo para enriquecer la aplicación).

## **Obligatorias**

### **Método GET**

A través del método GET en Postman, se pueden llevar a cabo dos de las cinco funciones implementadas. Respectivamente, estas funcionalidades son:

**Obtener Códigos de Error** , la cual retorna una lista con los errores que se han programado para la API, dando el código del error junto a su respectivo mensaje. Esta funcionalidad puede ser utilizada a través de la siguiente URL: **http://localhost:8080/usuarios/codigos**.

**Autenticar Usuario** , la cual pide un Email y una Contraseña y retorna un JSON, el cual será TRUE o FALSE dependiendo de si el usuario que se ha ingresado existe en la BD o no. La funcionalidad puede ser accedida a través de la siguiente URL, donde {email} y {password} son los campos donde se ingresan los datos requeridos previamente: **http://localhost:8080/usuarios/{email}/{password}**.

Ej: **http://localhost:8080/usuarios/pepe123@gmail.com/123456.**

### **Método POST**

A través del método POST en Postman, se puede crear nuevos usuarios con la funcionalidad **Crear Usuario**.

Esta funcionalidad requiere previamente que se le pasen los datos del usuario en un JSON de tipo Usuario, el cual contiene Email, Nombre, Apellido, Password y Roles, aunque para esta función solamente se piden los cuatro primeros atributos.

Para poder acceder a la funcionalidad, se hace uso de la URL raíz con el método POST y un Body en formato JSON con los datos del usuario.

Ej: **{**

" **email": "pepe@gmail.com",**

**"nombre": "Pepe",**

**"apellido": "Sanchez",**

**"password": "123456"**

**}**

Y en el caso de que el usuario ya exista, se lanza una excepción con código de error 101.

### **Método PUT**

A través del método PUT en Postman, se puede **Agregar o Eliminar Roles** a los usuarios que estén registrados en la BD del sistema.

El proceso de ambas funcionalidades es bastante parecido, en el caso de **Agregar Roles** se pasa un DTO a la función, el cual contiene el Email, Password y la lista de Roles a agregar al usuario en cuestión, y el sistema lo agrega a la lista de roles del usuario.

La URL de acceso a esta funcionalidad es: **http://localhost:8080/usuarios/agregarRoles**.

Por otro lado, **Eliminar Roles** hace exactamente lo mismo que Agregar Roles pero a la inversa, además de que se agrega un control antes de la eliminación de roles para comprobar que el usuario tenga todos los roles que se especifican en el DTO, y en el caso de encontrar algún rol que no tenga el usuario se lanza una excepción con código de error 103.

La URL de acceso a esta funcionalidad es: **http://localhost:8080/usuarios/eliminarRoles**.

Ej: **{**

**"email": "pepe@gmail.com,**

**"password": "123456,**

**"roles": ["ingeniero", "doctor", "constructor"]**

**}**

Las dos funcionalidades previamente descritas comparten dos excepciones respecto a la existencia del usuario que se ingresa en el DTO (con código de error 102) y al correcto ingreso de la contraseña de este mismo (con código de error 104), las cuales se lanzan respectivamente si ocurre alguno de estos casos.

##


## **Extras**

### **Método Delete**

A través del método DELETE en Postman, se puede **Eliminar Usuario por Id** o bien **Vaciar la BD** del sistema.

La funcionalidad de **Eliminar Usuario por Id** requiere únicamente el Email del usuario a eliminar.

Esta funcionalidad puede ser utilizada a través de la siguiente URL: **http://localhost:8080/usuarios/{email}** , dónde {email} es el Email del usuario a eliminar.

Ej: **http://localhost:8080/usuarios/pepe123@gmail.com**

En cambio, la funcionalidad de **Vaciar la BD** no requiere de ningún parámetro previo para su ejecución, y puede ser utilizada a través de la siguiente URL: **http://localhost:8080/usuarios/eliminarUsuarios**.

## **IDE, Lenguaje de Programación y Base de Datos que se utilizaron**

Para la realización del presente obligatorio se decidió utilizar el IDE "IntelliJ IDEA" debido a la experiencia previa de algunos de los integrantes del equipo con este mismo, además de que cuenta con funcionalidades muy útiles, las cuales además facilitan la introducción a este IDE.

![](RackMultipart20221107-1-i9dw1y_html_e6c746176f89b5b7.png)

El Lenguaje de Programación utilizado por el equipo fue Java. Este lenguaje se eligió debido principalmente a la experiencia y práctica que tiene el grupo con este mismo, además de que también fue utilizado en cursos previos de la carrera.

![](RackMultipart20221107-1-i9dw1y_html_217e9a467c22c3c6.png)

Para la Base de Datos, se decidió utilizar MongoDB. Esto se debe a la experiencia laboral de algunos de los integrantes del grupo, además de que esta BD cuenta con una interfaz gráfica muy amigable para el usuario, donde se pueden ver los documentos creados, además de poder eliminar BDs de manera sencilla.

![](RackMultipart20221107-1-i9dw1y_html_401f5fe076bc5317.png)
