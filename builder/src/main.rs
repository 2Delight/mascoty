mod config;
mod mascot;
#[macro_use]
mod utils;

extern crate log;
extern crate rand;
extern crate serde;
extern crate serde_json;
extern crate serde_yaml;
extern crate simple_logger;

use config::config::{import_config};

use log::{debug, info, warn, error};
use simple_logger::SimpleLogger;

use crate::mascot::get_mascot;
use grpc::mascot_server::{Mascot, MascotServer};
use grpc::{MascotRequest, MascotResponse};
use tonic::{transport::Server, Request, Response, Status};

pub mod grpc {
    tonic::include_proto!("mascot");
}

#[derive(Debug, Default)]
pub struct MascotService {}

#[tonic::async_trait]
impl Mascot for MascotService {
    async fn get_mascot(
        &self,
        _: Request<MascotRequest>,
    ) -> Result<Response<MascotResponse>, Status> {
        let mascot = get_mascot();
        info!("Sending response: {:?}", mascot);

        Ok(Response::new(MascotResponse{
            emotion: mascot.emotion,
            blink: mascot.blink,
            lips: mascot.lips,
            voice: mascot.voice as u32,
        }))
    }
}

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
