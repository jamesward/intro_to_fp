import play.libs.ws.StandaloneWSResponse;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static stuff.JavaStuff.*;

class JavaApp {

    public static void main(String[] args) {
        var i = addOne(2);

        var j = addOne(2);

        var k = addOne(2);

        var l = addOne(2);

        var m = addOne(2);
    }
}
