#[macro_export]
macro_rules! check_error {
    ($e:expr, $s:expr) => {
        match $e {
            Ok(val) => {
                crate::debug!("Success during {}: {:?}", $s, val);
                val
            },
            Err(err) => {
                crate::error!("Error during {}: {:?}", $s, err);
                return;
            },
        }
    };
}

#[macro_export]
macro_rules! panic_error {
    ($e:expr, $s:expr) => {
        match $e {
            Ok(val) => {
                // crate::debug!("Success during {}: {:?}", $s, val);
                val
            },
            Err(err) => {
                crate::error!("Error during {}: {:?}", $s, err);
                panic!();
            },
        }
    };
}
