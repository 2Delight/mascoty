use std::fs::File;
use std::error::Error;
use serde_yaml;
use serde::{Serialize, Deserialize};

#[derive(Debug, Serialize, Deserialize)]
pub struct Config {
    pub server: Server,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct Server {
    pub url: String,
    pub port: u16,
}

pub fn import_config() -> Result<Config, Box<dyn Error>> {
    let file = File::open("src/config/config.yaml")?;

    match serde_yaml::from_reader(file) {
        Ok(conf) => Ok(conf),
        Err(err) => Err(Box::new(err)),
    }
}
