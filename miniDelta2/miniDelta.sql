----Tablas independientes----
CREATE TABLE Habitaciones (
    Numero_Habitacion INT PRIMARY KEY,
    Descripcion VARCHAR2(255),
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
    Numero_Habitacion INT
);

ALTER TABLE PacientesBloom
ADD CONSTRAINT fk_pacientes_habitaciones
FOREIGN KEY (Numero_Habitacion) REFERENCES Habitaciones(Numero_Habitacion);

ALTER TABLE Pacientes_Medicamentos
ADD CONSTRAINT fk_pacientes_medicamentos_pacientes
FOREIGN KEY (ID_Paciente) REFERENCES PacientesBloom(ID_Paciente);

ALTER TABLE Pacientes_Medicamentos
ADD CONSTRAINT fk_pacientes_medicamentos_medicamentos
FOREIGN KEY (ID_Medicamento) REFERENCES Medicamentos(ID_Medicamento);