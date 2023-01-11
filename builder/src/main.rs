mod config;
mod input;
mod mascot;
mod service;
#[macro_use]
mod utils;

extern crate log;
extern crate nokhwa;
extern crate rand;
extern crate serde;
extern crate serde_json;
extern crate serde_yaml;
extern crate simple_logger;

use config::config::{import_config};

use log::{debug, info, warn, error};
use simple_logger::SimpleLogger;

use input::{Devices, Input, get_devices, get_input};

use service::MascotService;
use service::grpc::mascot_server::MascotServer;
use tonic::{transport::Server};

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    match SimpleLogger::new().with_level(log::LevelFilter::Debug).init() {
        Err(err) => panic!("Cannot initialize logger: {:?}", err),
        _ => {}
    };

    info!("Config parsing");
    let conf = panic_error!(import_config("src/config/config.yaml"), "config parsing");

    let devices = panic_error!(get_devices(), "getting devices");

    info!("Server was waken up");
    Server::builder()
        .add_service(
            MascotServer::new(
                MascotService{
                    devices: devices,
                },
            ),
        )
        .serve(format!("{}:{}", conf.server.url, conf.server.port).parse()?)
        .await?;

    info!("Server was shut down");
    Ok(())
}
