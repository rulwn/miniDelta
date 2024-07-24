----Tablas independientes----
CREATE TABLE Habitaciones (
    id_habitacion INT PRIMARY KEY,
    Numero_Habitacion INT,
    Numero_Cama INT
);

CREATE TABLE Medicamentos (
    ID_Medicamento INT PRIMARY KEY,
    Nombre_Medicamento VARCHAR2(50),
    Descripcion VARCHAR2(255)
);
----Tablas dependientes----


CREATE TABLE Pacientes_Medicamentos (
    ID_pacientes_medicamentos INT PRIMARY KEY,
    ID_Paciente INT,
    ID_Medicamento INT,
    Hora_Aplicacion DATE
);

CREATE TABLE PacientesBloom (
    ID_Paciente INT PRIMARY KEY,
    Nombres VARCHAR2(50),
    Apellidos VARCHAR2(50),
    Edad INT,
    Enfermedad VARCHAR2(50),
    id_habitacion INT
);

ALTER TABLE PacientesBloom
ADD CONSTRAINT fk_pacientes_habitaciones
FOREIGN KEY (id_habitacion) REFERENCES Habitaciones(id_habitacion) ON DELETE CASCADE;

ALTER TABLE Pacientes_Medicamentos
ADD CONSTRAINT fk_pacientes_medicamentos_pacientes
FOREIGN KEY (ID_Paciente) REFERENCES PacientesBloom(ID_Paciente) ON DELETE CASCADE;

ALTER TABLE Pacientes_Medicamentos
ADD CONSTRAINT fk_pacientes_medicamentos_medicamentos
FOREIGN KEY (ID_Medicamento) REFERENCES Medicamentos(ID_Medicamento) ON DELETE CASCADE;

CREATE SEQUENCE pacientesBloomSeq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE habitacionesBloomSeq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE MedicamentosBloomSeq
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER Trigger_Pacientes 
BEFORE INSERT ON PacientesBloom 
FOR EACH ROW 
BEGIN 
    SELECT pacientesBloomSeq.NEXTVAL 
    INTO: NEW.ID_Paciente 
    FROM DUAL;
END;
CREATE OR REPLACE TRIGGER Trigger_habitaciones
BEFORE INSERT ON habitaciones 
FOR EACH ROW 
BEGIN 
    SELECT habitacionesBloomSeq.NEXTVAL 
    INTO: NEW.id_habitacion 
    FROM DUAL;
END;
CREATE OR REPLACE TRIGGER Trigger_medicamentos
BEFORE INSERT ON Medicamentos 
FOR EACH ROW 
BEGIN 
    SELECT MedicamentosBloomSeq.NEXTVAL 
    INTO: NEW.id_medicamento
    FROM DUAL;
END;


INSERT ALL
    INTO Habitaciones (Numero_Habitacion, Numero_Cama) VALUES (1, 1)
    INTO Habitaciones (Numero_Habitacion, Numero_Cama) VALUES (2, 2)
    INTO Habitaciones (Numero_Habitacion, Numero_Cama) VALUES (3, 1)
    INTO Habitaciones (Numero_Habitacion, Numero_Cama) VALUES (3, 1)
SELECT 1 FROM DUAL;
INSERT ALL
    INTO PacientesBloom (Nombres, Apellidos, Edad, Enfermedad, id_habitacion) VALUES ('Ana Carla', 'Gómez Lopez', 30, 'Asma', 1)
    INTO PacientesBloom (Nombres, Apellidos, Edad, Enfermedad, id_habitacion) VALUES ('Carlos Alberto', 'López Lozano', 50, 'Diabetes', 2)
    INTO PacientesBloom (Nombres, Apellidos, Edad, Enfermedad, id_habitacion) VALUES ('María Manzana', 'Rodríguez Patudo', 40, 'Hipertensión', 3)
SELECT DUMMY FROM DUAL;

select * from medicamentos;
INSERT ALL
    INTO Medicamentos (Nombre_Medicamento, Descripcion)
         VALUES ('Paracetamol', 'Medicamento utilizado para reducir la fiebre y aliviar el dolor.')
    INTO Medicamentos (Nombre_Medicamento, Descripcion)
         VALUES ('Ibuprofeno', 'Anti-inflamatorio no esteroideo usado para aliviar el dolor y la inflamación.')
    INTO Medicamentos (Nombre_Medicamento, Descripcion)
         VALUES ('Amoxicilina', 'Antibiótico utilizado para tratar diversas infecciones bacterianas.')
    INTO Medicamentos (Nombre_Medicamento, Descripcion)
         VALUES ('Omeprazol', 'Inhibidor de la bomba de protones que reduce la cantidad de ácido en el estómago.')
    INTO Medicamentos (Nombre_Medicamento, Descripcion)
         VALUES ('Clorfenamina', 'Antihistamínico usado para aliviar los síntomas de las alergias.')
SELECT DUMMY FROM DUAL;






DROP TABLE Pacientes_Medicamentos CASCADE CONSTRAINTS;

-- Luego elimina las tablas principales
DROP TABLE PacientesBloom CASCADE CONSTRAINTS;
DROP TABLE Habitaciones CASCADE CONSTRAINTS;
DROP TABLE Medicamentos CASCADE CONSTRAINTS;

-- Elimina las secuencias
DROP SEQUENCE pacientesBloomSeq;
DROP SEQUENCE habitacionesBloomSeq;
DROP SEQUENCE MedicamentosBloomSeq;

DELETE FROM PacientesBloom WHERE ID_Paciente = 1

-- Elimina los triggers
DROP TRIGGER Trigger_Pacientes;
DROP TRIGGER Trigger_habitaciones;


Select * from PacientesBloom;
Select * from habitaciones;
SELECT p.ID_Paciente, p.Nombres, p.Apellidos, p.Edad, p.Enfermedad,h.id_habitacion, h.Numero_Habitacion, h.Numero_Cama FROM PacientesBloom p INNER JOIN Habitaciones h ON p.id_habitacion = h.id_habitacion
