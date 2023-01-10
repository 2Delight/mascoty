use nokhwa::query;
use nokhwa::utils::{ApiBackend, CameraFormat, FrameFormat, RequestedFormat, RequestedFormatType};
use nokhwa::Camera;
use nokhwa::pixel_format::RgbFormat;
use nokhwa::NokhwaError;
use crate::panic_error;

pub struct Devices {
    camera: Camera,
}

pub struct Input {
}

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
            camera: camera,
        },
    )
}

pub fn get_input() -> Result<Input, NokhwaError> {
    let mut devices = panic_error!(get_devices(), "getting devices");

    let frame = devices.camera.frame()?;
    Ok(Input{})
}
