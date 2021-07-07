CREATE TABLE IF NOT EXISTS PUBLIC.host_info (
    id SERIAL NOT NULL,
    hostname VARCHAR NOT NULL,
    cpu_number INTEGER NOT NULL,
    cpu_architecture VARCHAR NOT NULL,
    cpu_model VARCHAR NOT NULL,
    cpu_mhz DECIMAL NOT NULL,
    l2_cache INTEGER NOT NULL,
    total_mem INTEGER NOT NULL,
    "timestamp" TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage (
    "timestamp" TIMESTAMP NOT NULL,
    host_id INTEGER NOT NULL,
    memory_free INTEGER NOT NULL,
    cpu_idle INTEGER NOT NULL,
    cpu_kernel INTEGER NOT NULL,
    disk_io INTEGER NOT NULL,
    disk_available INTEGER NOT NULL,
    FOREIGN KEY(host_id)
        REFERENCES host_info(id)
        ON DELETE CASCADE
);