package app;

import app.config.NettyServerProvider;
import app.service.impl.ProductService;
import app.service.impl.UserService;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER =
            Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws MalformedURLException {

        final String httpServer = NettyServerProvider.startHttpServer(
                // Один ресурс
//                UserService.class
                // Або декілька ресурсів
                UserService.class, ProductService.class
        );

        LOGGER.log(Level.INFO, httpServer);
    }
}
