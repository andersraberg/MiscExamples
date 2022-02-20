package se.anders_raberg.junittest;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class MyArgumentsProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
		return Stream.of( //
				arguments(1, 1), //
				arguments(2, 4), //
				arguments(3, 9), //
				arguments(12, 144));
	}
}
