use crate::input::Devices;
use crate::mascot::get_mascot;

use grpc::mascot_server::Mascot;
use grpc::{MascotRequest, MascotResponse};

use std::sync::Mutex;
use std::sync::Arc;

use log::{debug, info, warn, error};

use tonic::{Request, Response, Status};

pub mod grpc {
    tonic::include_proto!("mascot");
}

#[derive(Debug, Default)]
pub struct MascotService {
    // devices: Devices,
    // devices: Arc<Mutex<Devices>>,
}

#[tonic::async_trait]
impl Mascot for MascotService {
    async fn get_mascot(&self, _: Request<MascotRequest>) -> Result<Response<MascotResponse>, Status> {
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
