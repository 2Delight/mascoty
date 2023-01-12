mod config;
mod input;
mod mascot;
mod service;
#[macro_use]
mod utils;

extern crate dotenv;
extern crate log;
extern crate nokhwa;
extern crate rand;
extern crate serde;
extern crate serde_json;
extern crate serde_yaml;
extern crate simple_logger;

use config::config::import_config;
use input::get_devices;

use log::{debug, error, info, warn};
use simple_logger::SimpleLogger;
use dotenv::dotenv;

use service::grpc::mascot_server::MascotServer;
use service::MascotService;
use tonic::transport::Server;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    match SimpleLogger::new()
        .with_level(log::LevelFilter::Debug)
        .init() {
        Ok(()) => {},
        Err(err) => panic!("Cannot initialize logger: {:?}", err),
    };

    match dotenv().ok() {
        Some(_) => debug!("Successfully imported data from .env"),
        None => debug!("Failed to work with .env"),
    };

    debug!("Config parsing");
    let conf = panic_error!(import_config("src/config/config.yaml"), "config parsing");
    info!("Config: {:?}", conf);

    debug!("Getting devices");
    let devices = panic_error!(get_devices(&conf), "getting devices");

    debug!("Server was waken up");
    Server::builder()
        .add_service(MascotServer::new(MascotService { devices: devices }))
        .serve(format!("{}:{}", conf.server.url, conf.server.port).parse()?)
        .await?;

    debug!("Server was shut down");
    Ok(())
}
