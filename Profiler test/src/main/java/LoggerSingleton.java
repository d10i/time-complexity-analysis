public class LoggerSingleton {
    public static Logger logger;

    public Logger get() {
        if (logger == null) {
            logger = new Logger();
        }

        return logger;
    }
}
