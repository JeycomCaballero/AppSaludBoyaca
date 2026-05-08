-- Consultas SQL
-- INSERT 
use biblioteca;

-- insert libro / autor

insert into editorial (nombre,pais,sitio_web) values ('Reynal & Hitchcock', 'NuevaYork' , 'Reynal.com');
insert into categoria (nombre,descripcion) values ('Ficcion', 'Fantastico');
insert into libro (titulo,isbn,año_publicacion,num_paginas,id_editorial,disponible,id_categoria) values ('El principito',
'232323asddda', '1943-04-06' , 120, 1,1,1);
insert into autor (nombres,apellidos,nacionalidad,fecha_nacimiento) values ('Antonie','de Saint', 'Frances', '1900-06-29');

-- insert general de libros, categorias y autores

-- editoriales
insert into editorial (nombre,pais,sitio_web) values 
('Penguin Random House', 'Estados Unidos', 'penguinrandomhouse.com'),
('Planeta', 'España', 'planeta.es'),
('HarperCollins', 'Estados Unidos', 'harpercollins.com');

-- categorias  

insert into categoria (nombre,descripcion) values
('Aventura', 'Historias de exploracion y viajes'),
('Ciencia Ficcion', 'Relatos basados en tecnologia o futuro'),
('Clasico', 'Obras literarias reconocidas mundialmente');

-- autores 

insert into autor (nombres,apellidos,nacionalidad,fecha_nacimiento) values
('Julio','Verne','Frances','1828-02-08'),
('Gabriel','Garcia Marquez','Colombiano','1927-03-06'),
('George','Orwell','Britanico','1903-06-25'),
('J.R.R.','Tolkien','Britanico','1892-01-03');

-- libros 

insert into libro (titulo,isbn,año_publicacion,num_paginas,id_editorial,disponible,id_categoria) values
('Viaje al centro de la Tierra','9780451532152','1864-11-25',300,2,1,2),
('Cien años de soledad','9780307474728','1967-05-30',417,2,1,1),
('1984','9780451524935','1949-06-08',328,3,1,2),
('El señor de los anillos','9780618640157','1954-07-29',1178,1,1,3),
('La isla misteriosa','9780140449181','1874-01-01',650,2,1,2);


-- insert libro-autor

insert into libro_autor (id_libro,id_autor) values(1,1), (2,2), (3,3), (4,4), (5,5), (6,2); 

-- insert usuario

insert into rol (tipoRol) values ('Usuario');
insert into usuario (documento,nombre,apellido,email,telefono,estado,id_rol) values (23232323,'Jose','Miguel', 'joseM@gmail.com', '3224326545', 1,1);


-- insert bibliotecario

insert into rol (tipoRol) values ('Bibliotecario');
insert into usuario (documento,nombre,apellido,email,telefono,estado,id_rol) values (2312333,'Fernando', 'Rosario','fernandoR@gmail.com','2323332',1,2);


-- Update
-- cambio de telefoo

update usuario set telefono = 2344443 where documento = 2312333;	

-- delete 

delete from usuario  where documento = 2312333;
 
--  Listar todos los libros con su editorial (INNER JOIN)

select lb.titulo,lb.isbn ,ed.nombre,ed.pais from libro as lb inner join editorial  as ed on lb.id_editorial = ed.id_editorial;

-- Listar libros con sus autores (JOIN con tabla intermedia)

select lb.titulo,au.nombres from libro as lb inner join libro_autor as la on lb.id_libro = la.id_libro inner join autor as au on la.id_autor = au.id_autor;

--  Contar libros por categoría (GROUP BY)

select cg.nombre, count(lb.id_libro) as cantidasLibro from libro as lb  inner join categoria as cg on lb.id_categoria = cg.id_categoria group by cg.nombre;


--  Listar préstamos vencidos (WHERE con fechas)

select us.nombre,pm.fecha_devolucion_esperada as FechaDevolucionFija, pm.fecha_devolucion_real  as fechaDevolucionUsuario from usuario as us inner join prestamo as pm on us.id_usuario = pm.id_usuario where pm.fecha_devolucion_real > pm.fecha_devolucion_esperada;

-- Calcular total de multas por usuario (SUM, GROUP BY)

select us.nombre, sum(mu.monto) as multasTotalesUsuario from usuario as us  inner join prestamo as pm on us.id_usuario = pm.id_usuario inner join multa as mu on pm.id_prestamo = mu.id_prestamo group by us.nombre;

-- Buscar libros por título o autor (LIKE, OR)

select lb.titulo, au.nombres from libro as lb inner join libro_autor as la on lb.id_libro = la.id_libro inner join autor as au on la.id_autor = au.id_autor where au.nombres like '%%' or au.apellidos like '%%' or lb.titulo like '%%';

-- Listar usuarios con préstamos activos (EXISTS o IN con subconsulta)

select us.nombre from usuario as us where us.id_usuario in (select pm.id_usuario from prestamo as pm where pm.estado = 1);

--  Top 10 libros más prestados (ORDER BY, LIMIT)

select lb.titulo ,count(lb.id_libro) as cantidadPrestamo from libro as lb inner join prestamo as pm on lb.id_libro = pm.id_libro group by lb.titulo order by cantidadPrestamo desc limit 10;

-- left Join 

select lb.titulo, au.nombres from libro as lb left join libro_autor as lu on lb.id_libro = lu.id_libro left join autor as au on lu.id_autor = au.id_autor;

-- righ join

select ct.nombre,lb.titulo from categoria as ct right join libro as lb on ct.id_categoria = lb.id_categoria;

-- having 

select  ct.nombre, count(lb.id_libro) as cantidadCategoria from libro as lb inner join categoria as ct on lb.id_categoria = ct.id_categoria group by ct.nombre having cantidadCategoria > 2;  


--  procedimientos almacenados 

-- 1. 
DELIMITER //

create procedure registrarLibro(
in tituloLib varchar(50),
in isbnL varchar (50),
in añoPL date,
in nuPaginas int,
in idEditorial int,
in disponibilidadL tinyint,
in idCategoria int
)
begin

insert into libro (titulo,isbn,año_publicacion,num_paginas,id_editorial,disponible,id_categoria) values (tituloLib,isbnL,añoPL,nuPaginas,idEditorial,disponibilidadL,idCategoria);
end //
DELIMITER ;

-- 2.
DELIMITER //

create procedure registrarUsuario (
in documentoU int,
in nombreU varchar(70),
in apellidoU varchar(70),
in emailU varchar(90),
in telefonoU varchar(20),
in estadoU tinyint(1),
in idRolU int
)
begin
insert into usuario (documento,nombre,apellido,email,telefono,estado,id_rol) values (documentoU,nombreU,apellidoU,emailU,telefonoU,estadoU,idRolU);
end //
DELIMITER ;

-- 3.
DELIMITER //
create procedure registrarPrestamo (
in idLibro int,
in idUsuario int,
in fechaPrestamo date,
in fechaDevolucionEsperada date,
in fechaDevolucionReal date,
in estadoP tinyint
)
begin 
insert into prestamo (id_libro,id_usuario,fecha_prestamo,fecha_devolucion_esperada,fecha_devolucion_real, estado) values (idLibro,idUsuario,fechaPrestamo,fechaDevolucionEsperada,fechaDevolucionReal,estadoP);
end //
DELIMITER ;

-- triggers

-- 1
DELIMITER //
 
create trigger prestamoLibrosN 
after insert on prestamo 
for each row 
begin 
update libro set disponible = 0 where id_libro = new.id_libro;
end //
DELIMITER ;

-- 2

DELIMITER //
create trigger prestamoLibrosD
after update on prestamo
for each row
begin
if new.fecha_devolucion_real is not null then
update libro set disponible = 1 where id_libro = new.id_libro;
end if;
end //
DELIMITER ;

-- 3

DELIMITER //
create trigger validacionUsuario 
before insert on usuario
for each row
begin
if new.documento is null or new.documento = '' then signal sqlstate '45000'
set message_text = 'No se puede insertar un documento vacio';
end if;
end //
DELIMITER ;

-- vistas 
-- 1. Prestamos Usuarios

create view prestamosUsuario as
select us.nombre,pm.fecha_devolucion_esperada,pm.fecha_devolucion_real,lb.titulo from usuario as us 
inner join prestamo as pm on us.id_usuario = pm.id_usuario
inner join libro as lb on pm.id_libro = lb.id_libro;

-- 2. multas usuario 

create view multaUsuarios as 
select us.nombre, lb.titulo, mu.monto from usuario as us inner join prestamo as pm on us.id_usuario = pm.id_usuario inner join
multa as mu on pm.id_prestamo = mu.id_prestamo inner join libro as lb on pm.id_libro = lb.id_libro;

-- 3. prestamos retrasados

create view prestamoRetrasado as
select us.nombre from usuario as us inner join prestamo as pm on us.id_usuario = pm.id_usuario 
where pm.fecha_devolucion_real > pm.fecha_devolucion_esperada;





