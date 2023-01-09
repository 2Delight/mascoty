use nokhwa::{Camera, CameraFormat, CaptureAPIBackend, FrameFormat, NokhwaError, query_devices};

pub struct Devices {
    camera: Camera,
}

pub struct Input {
}

pub fn get_devices() -> Result<Devices, NokhwaError> {
    let cams = query_devices(CaptureAPIBackend::Auto)?;
    println!("{}", cams.len());
    println!("{}", cams[0].index());

    if cams.len() == 0 {
        return Err(
            NokhwaError::GeneralError(
                "Cannot find any connected camera!".to_string(),
            ),
        );
    }

    let mut camera = Camera::new(
        cams[0].index(),
        Some(
            CameraFormat::new_from(1280, 720, FrameFormat::MJPEG, 30)
        ),
    )
    .unwrap();

    println!("{}", camera.info());

    camera.open_stream()?;

    Ok(
        Devices
        {
            camera: camera,
        },
    )
}

pub fn get_input(devices: &mut Devices) -> Result<Input, NokhwaError> {
    let frame = devices.camera.frame()?;
    frame.into_vec();
    Ok(Input{})
}
