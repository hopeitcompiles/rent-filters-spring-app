# Filters Spring Thymeleaf MySQL
Esta aplicación usa Spring Framework, Thymeleaf y una base de datos MySQL para organizar un sistema de alquiler de dispositivos, donde cada uno de estos puede ser rentado( en caso de estar disponible) estableciendo una fecha de inicio, fin (límite de dovolución) y de retorno. Solo se pueden rentar los dispositivos que no están ya registrados en un alquiler que no ha sido devuelto. Se aplicará una multa de 5 dólares por día en caso de atrasos en la devolución.
NO INCLUYE INGRESO O MODIFICACIÓN DE DATOS, SOLO VISUALIZACIÓN MEDIANTE FILTROS.

### Modelo entidad relación:
<img src="https://user-images.githubusercontent.com/73175815/152663974-ca8311fa-2fc0-4305-b522-63255e0269f4.png" width="300">

Los filtros aplicables al listado de rentas son los siguientes: 
Rango de Fecha, dispositivo, está multado o no, dispositivo devuelto o no. Todos estos filtros se pueden combinar.

### Los filtros
Estos se aplican mediante EntityManager, se genera una Query mediante CriteriaBuilder la cual contiene todos los parámetros que el usuario establezca.

Aplicación funcional puede ser probada aquí:
https://cake-rent-filters.herokuapp.com

En caso de no estar disponible, es probable que se haya excedido el límite de consultas de la base de datos ClearDB.

### Filtrado de pocos atributos
En caso de que solo se requiera aplicar filtro a pocos atributos, siempre y cuando no contengan parámetros null, es posible crear un método que describa la consulta en la interfaz de repositorio, si dicho repositorio extiende de JPARepository es posible también incluir la paginación. Por ejemplo:

<img src="https://user-images.githubusercontent.com/73175815/152672253-b964c2e3-dae4-424a-b98f-4b32b6a13020.png">

Sin la necesidad de incluir código extra, solo indicándolo mediante el nombre del método en la interfaz de repositorio, Spring reconocerá la consula y traerá los resultados.

### Filtrado de multiples atributos
Cuando se tienen varios parámetros y su combinación puede varias, se puede emplear CriteriaBuilder del EntityManager, CriteriaQuery, Root y una lista de Predicate que incluyan los parámetros de filtro a aplicar. Por ejemplo:


<img src="https://user-images.githubusercontent.com/73175815/152672386-f9bb72f2-eda0-4259-8331-be0c805fc309.png">

Este método permite crear distintas combinaciones personalizadas, incluyendo controles OR y AND, para aplicar los filtros deseados.

