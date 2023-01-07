macro_rules! check_error {
    ($e:expr, $s:expr) => {
        match $e {
            Ok(val) => {
                debug!("Success during {}: {:?}", $s, val);
                val
            },
            Err(err) => {
                error!("Error during {}: {:?}", $s, err);
                return;
            },
        }
    };
}

macro_rules! panic_error {
    ($e:expr, $s:expr) => {
        match $e {
            Ok(val) => {
                debug!("Success during {}: {:?}", $s, val);
                val
            },
            Err(err) => {
                error!("Error during {}: {:?}", $s, err);
                panic!();
            },
        }
    };
}
