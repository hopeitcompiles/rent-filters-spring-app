# filters-spring-thymeleaf-mysql
Esta aplicación usa Spring Framework, Thymeleaf y una base de datos MySQL para organizar un sistema de alquiler de dispositivos, donde cada uno de estos es rentado estableciendo una fecha de inicio, fin (límite de dovolución) y de retorno. Solo se pueden rentar los dispositivos que no están ya registrados en un alquiler que no ha sido devuelto. Se aplicará una multa de 5 dólares por día en caso de atrasos en la devolución.

### Modelo entidad relación:
<img src="https://user-images.githubusercontent.com/73175815/152663974-ca8311fa-2fc0-4305-b522-63255e0269f4.png" width="300">

Los filtros aplicables al listado de rentas son los siguientes: 
Rango de Fecha, dispositivo, está multado o no, dispositivo devuelto o no. Todos estos filtros se pueden combinar.

### Los filtros
Estos se aplican mediante EntityManager, se genera una Query mediante CriteriaBuilder la cual contiene todos los parámetros que el usuario establezca.

Aplicación funcional puede ser probada aquí:
https://cake-rent-filters.herokuapp.com

En caso de no estar disponible, es probable que se haya excedido el límite de consultas de la base de datos ClearDB.


