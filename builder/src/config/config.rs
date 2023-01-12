use std::fs::File;
use std::error::Error;

use log::{debug, info, warn, error};
use serde_yaml;
use serde::{Serialize, Deserialize};

#[derive(Debug, Serialize, Deserialize)]
pub struct Config {
    pub server: Server,
    pub camera: Camera,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct Server {
    pub url: String,
    pub port: u16,
}

#[derive(Debug, Serialize, Deserialize)]
pub struct Camera {
    pub height: u32,
    pub width: u32,
    pub fps: u32,
}

pub fn import_config(path: &str) -> Result<Config, Box<dyn Error>> {
    debug!("Reading config file");
    let file = File::open(path)?;

    debug!("Deserializing YAML");
    match serde_yaml::from_reader(file) {
        Ok(conf) => Ok(conf),
        Err(err) => Err(Box::new(err)),
    }
}

#[cfg(test)]
mod tests {
    use crate::config::config::import_config;

    #[test]
    fn check_config_right_path() {
        import_config("src/config/config.yaml").unwrap();
    }

    #[test]
    #[should_panic]
    fn check_config_wrong_path() {
        import_config("it is not even a path lol").unwrap();
    }

    #[test]
    fn check_url() {
        let conf = import_config("src/config/config.yaml").unwrap();
        assert!(conf.server.url.len() > 0);
    }

    #[test]
    fn check_port() {
        let conf = import_config("src/config/config.yaml").unwrap();
        assert!(conf.server.port > 0);
    }
}
