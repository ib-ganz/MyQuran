package ib.ganz.myquran.network;

import com.google.gson.stream.MalformedJsonException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by GrT on 6/7/2017.
 */

public class MyErrorHandlerFactory// extends CallAdapter.Factory
{
//    private final RxJava2CallAdapterFactory original;
//
//    private MyErrorHandlerFactory()
//    {
//        original = RxJava2CallAdapterFactory.create();
//    }
//
//    public static CallAdapter.Factory create()
//    {
//        return new MyErrorHandlerFactory();
//    }
//
//    @Override
//    public CallAdapter<?> getLocation(Type returnType, Annotation[] annotations, Retrofit retrofit)
//    {
//        return new RxCallAdapterWrapper(retrofit, original.getLocation(returnType, annotations, retrofit));
//    }
//
//    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {
//        private final Retrofit retrofit;
//        private final CallAdapter<?> wrapped;
//
//        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped) {
//            this.retrofit = retrofit;
//            this.wrapped = wrapped;
//        }
//
//        @Override
//        public Type responseType() {
//            return wrapped.responseType();
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        public <R> Observable<?> adapt(Call<R> call) {
//            return ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, ObservableSource>()
//            {
//                @Override
//                public ObservableSource apply(Throwable throwable) throws Exception
//                {
//                    return Observable.error(asMyException(throwable));
//                }
//            });
//        }
//
//        private MyException asMyException(Throwable throwable) throws IOException
//        {
//            if (throwable instanceof MyException)
//            {
//                return (MyException)throwable;
//            }
//            else if (throwable instanceof HttpException)
//            {
//                HttpException httpException = (HttpException) throwable;
//                Response response = httpException.response();
//
//                return new MyException(throwable.getMessage(), response.errorBody().str());
//            }
//            else if (throwable instanceof SocketTimeoutException)
//            {
//                return new MyException("Network error. Please check your connection.");
//            }
//
//            return new MyException(throwable.getMessage());
//        }
//    }
}
