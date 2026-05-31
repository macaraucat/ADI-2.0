ALTER TABLE db_travel_service.pasajero 
ALTER COLUMN fecha_nacimiento TYPE DATE USING fecha_nacimiento::DATE;