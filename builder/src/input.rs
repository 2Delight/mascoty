use crate::panic_error;

use nokhwa::{Camera, NokhwaError, query};
use nokhwa::pixel_format::RgbFormat;
use nokhwa::utils::{ApiBackend, CameraFormat, FrameFormat, RequestedFormat, RequestedFormatType};

use std::sync::Mutex;

pub struct Devices {
    camera: Mutex<Camera>,
}

unsafe impl Sync for Devices {}

unsafe impl Send for Devices {}

pub struct Input {}

pub fn get_devices() -> Result<Devices, NokhwaError> {
    let cams = query(ApiBackend::Auto)?;

    println!("{}", cams.len());
    println!("{}", cams[0].index());

    if cams.len() == 0 {
        return Err(
            NokhwaError::GeneralError(
                "Cannot find any connected camera!".to_string(),
            ),
        );
    }

    let format_type = RequestedFormatType::Exact(CameraFormat::new_from(1280, 720, FrameFormat::MJPEG, 30));
    let format = RequestedFormat::new::<RgbFormat>(format_type);
    let mut camera = Camera::new(cams[0].index().to_owned(), format).unwrap();

    println!("{}", camera.info());

    camera.open_stream()?;

    Ok(
        Devices
        {
            camera: Mutex::new(camera),
        },
    )
}

pub fn get_input(devices: &Devices) -> Result<Input, NokhwaError> {
    let mut camera = panic_error!(devices.camera.lock(), "failed to lock mutex");
    let frame = camera.frame()?;

    Ok(Input{})
}
