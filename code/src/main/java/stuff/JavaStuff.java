package stuff;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.ActorMaterializerSettings;
import com.typesafe.config.ConfigFactory;
import play.libs.ws.ahc.AhcWSClientConfigFactory;
import play.libs.ws.ahc.StandaloneAhcWSClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaStuff {

    static Integer one = 0;

    public static Integer addOne(Integer i) {
        one++;

        if (one == 3) {
            return null;
        }
        else if (one == 4) {
            throw new IllegalStateException();
        }
        else {
            return i + one;
        }
    }

    public static Function<Integer, Integer> addTwo = i -> i + 2;

    public static Function<String, Integer> count = String::length;

    public static String randomNumUrl = "http://randnum.herokuapp.com";
    public static String randomWordUrl = "http://random-word.herokuapp.com/";

    public static Function<Function<StandaloneAhcWSClient, CompletionStage<?>>, CompletionStage<?>> ws = f -> {
        var system = ActorSystem.create();
        system.registerOnTermination(() -> System.exit(0));
        var settings = ActorMaterializerSettings.create(system);
        var materializer = ActorMaterializer.create(settings, system);

        var client = StandaloneAhcWSClient.create(AhcWSClientConfigFactory.forConfig(ConfigFactory.load(), system.getClass().getClassLoader()), materializer);

        var completionStage = f.apply(client);
        return completionStage.whenComplete((v, error) -> {
            try {
                System.out.println(v);
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).whenComplete((ignored, error) -> system.terminate());
    };

    public static<T> CompletableFuture<List<T>> sequence(Supplier<CompletableFuture<T>> generator, Integer num) {
        var all = Stream.generate(generator).limit(num).collect(Collectors.toList());

        return CompletableFuture.allOf(all.toArray(CompletableFuture[]::new))
                .thenApply(v -> all.stream().map(CompletableFuture::join).collect(Collectors.toList())
        );
    }

}
