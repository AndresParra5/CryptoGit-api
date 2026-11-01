-- Inserción de activos iniciales
INSERT INTO activos_crypto (simbolo, nombre, cantidad, precio_compra_eur, email_analista, fecha_adquisicion, fecha_revision, riesgo_aceptado)
VALUES ('bitcoin', 'Bitcoin', 1.5, 45000.00, 'analista@cryptogit.com', '2026-01-10', '2027-01-10', true);

INSERT INTO activos_crypto (simbolo, nombre, cantidad, precio_compra_eur, email_analista, fecha_adquisicion, fecha_revision, riesgo_aceptado)
VALUES ('ethereum', 'Ethereum', 10.0, 2800.00, 'analista@cryptogit.com', '2026-01-15', '2027-01-15', true);

INSERT INTO activos_crypto (simbolo, nombre, cantidad, precio_compra_eur, email_analista, fecha_adquisicion, fecha_revision, riesgo_aceptado)
VALUES ('cardano', 'Cardano', 500.0, 0.45, 'senior@cryptogit.com', '2026-02-01', '2026-12-31', true);

-- Inserción de etiquetas en singular: 'etiqueta'
INSERT INTO activo_crypto_etiquetas (activo_crypto_id, etiqueta) VALUES (1, 'BTC');
INSERT INTO activo_crypto_etiquetas (activo_crypto_id, etiqueta) VALUES (1, 'STORE_OF_VALUE');

INSERT INTO activo_crypto_etiquetas (activo_crypto_id, etiqueta) VALUES (2, 'SMART_CONTRACTS');
INSERT INTO activo_crypto_etiquetas (activo_crypto_id, etiqueta) VALUES (2, 'L1');

INSERT INTO activo_crypto_etiquetas (activo_crypto_id, etiqueta) VALUES (3, 'STAKING');
INSERT INTO activo_crypto_etiquetas (activo_crypto_id, etiqueta) VALUES (3, 'POS');