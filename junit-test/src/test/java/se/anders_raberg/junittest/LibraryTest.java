/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package se.anders_raberg.junittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryTest {

	@Mock
	MockedInterface _mockedInterface;

	@Captor
	private ArgumentCaptor<List<String>> _captor;

	@ParameterizedTest(name = "{index} ==> the square of {0} is {1}")
	@ArgumentsSource(MyArgumentsProvider.class)
	void testSquares(int n, int n2) {
		assertEquals(n2, Library.square(n));
	}

	@Test
	void testMock() {
		Mockito.lenient().when(_mockedInterface.convert(Mockito.anyInt())).thenReturn(4);
		assertEquals(4, _mockedInterface.convert(42));
		Mockito.verify(_mockedInterface).convert(Mockito.anyInt());
		_mockedInterface.set(10);
		_mockedInterface.set(20);
		_mockedInterface.set(30);
		Mockito.verify(_mockedInterface, Mockito.times(3)).set(Mockito.anyInt());
		Mockito.verifyNoMoreInteractions(_mockedInterface);
	}

	@Test
	void testList(@Mock List<String> mockedList) {
		List<String> list = List.of("alpha", "bravo", "charlie");
		mockedList.addAll(list);

		Mockito.verify(mockedList).addAll(_captor.capture());
		List<String> capturedArgument = _captor.getValue();
		assertTrue(capturedArgument.contains("bravo"));
	}

}
