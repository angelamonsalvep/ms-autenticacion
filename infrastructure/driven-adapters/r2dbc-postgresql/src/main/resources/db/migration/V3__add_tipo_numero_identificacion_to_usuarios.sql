ALTER TABLE usuarios
ADD COLUMN tipo_identificacion VARCHAR(20),
ADD COLUMN numero_identificacion BIGINT;

UPDATE usuarios SET tipo_identificacion = 'CC', numero_identificacion = 0 WHERE tipo_identificacion IS NULL OR numero_identificacion IS NULL;

ALTER TABLE usuarios
ALTER COLUMN tipo_identificacion SET NOT NULL,
ALTER COLUMN numero_identificacion SET NOT NULL;

ALTER TABLE usuarios
ADD CONSTRAINT usuarios_tipo_numero_identificacion_unique UNIQUE (tipo_identificacion, numero_identificacion);
