mod config;
mod mascot;
#[macro_use]
mod utils;
mod service;

extern crate log;
extern crate rand;
extern crate serde;
extern crate serde_json;
extern crate serde_yaml;
extern crate simple_logger;

use config::config::{import_config};

use log::{debug, info, warn, error};
use simple_logger::SimpleLogger;

use service::MascotService;
use service::grpc::mascot_server::{MascotServer};
use tonic::{transport::Server};

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    match SimpleLogger::new().with_level(log::LevelFilter::Info).init() {
        Err(err) => panic!("Cannot initialize logger: {:?}", err),
        _ => {}
    };

    info!("Config parsing");
    let conf = panic_error!(import_config(), "config parsing");

    info!("Server was waken up");
    Server::builder()
        .add_service(
            MascotServer::new(
                MascotService::default(),
            ),
        )
        .serve(format!("{}:{}", conf.server.url, conf.server.port).parse()?)
        .await?;

    info!("Server was shut down");
    Ok(())
}
