use crate::mascot::get_mascot;

use tonic::{Request, Response, Status};
use grpc::mascot_server::{Mascot};
use grpc::{MascotRequest, MascotResponse};

use log::{debug, info, warn, error};

pub mod grpc {
    tonic::include_proto!("mascot");
}

#[derive(Debug, Default)]
pub struct MascotService {

}

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
