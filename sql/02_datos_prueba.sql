USE servicedesk360_db;

INSERT INTO clientes (nombre, correo) VALUES
('Ana López', 'ana.lopez@demo.local'),
('Carlos Méndez', 'carlos.mendez@demo.local');

INSERT INTO categorias (nombre) VALUES
('Software'), ('Hardware'), ('Acceso');

INSERT INTO tecnicos (nombre, correo, especialidad) VALUES
('María Ruiz', 'maria.ruiz@demo.local', 'Aplicaciones web');