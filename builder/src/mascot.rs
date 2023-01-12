use crate::input::{get_input, Devices};
use crate::panic_error;

use rand::Rng;
use serde::{Serialize, Deserialize};
use log::{debug, error, info, warn};

#[derive(Debug, Serialize, Deserialize)]
pub struct Mascot {
    pub emotion: String,
    pub blink: bool,
    pub lips: bool,
    pub voice: u8,
}

fn face_ml() -> (String, bool, bool) {
    let mut rng = rand::thread_rng();
    (
        String::new(),
        rng.gen::<bool>(),
        rng.gen::<bool>(),
    )
}

fn voice_ml() -> (String, u8) {
    let mut rng = rand::thread_rng();
    (
        String::new(),
        rng.gen::<u8>() % 50,
    )
}

pub fn get_mascot(devices: &Devices) -> Mascot {
    debug!("Getting mascot");
    let input = panic_error!(get_input(devices), "getting input");

    let mut mascot = Mascot {
        emotion: String::new(),
        blink: true,
        lips: true,
        voice: 0,
    };

    let (emotion1, eyes, lips) = face_ml();
    let (emotion2, voice) = voice_ml();

    mascot.emotion = emotion1 + emotion2.as_str();
    mascot.blink = eyes;
    mascot.lips = lips;
    mascot.voice = voice;

    debug!("Mascot has been created");
    mascot
}
