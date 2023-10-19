package io.fxtras.svg;

import io.fxtras.svg.xml.parsers.SVGLibraryException;
import io.fxtras.svg.xml.parsers.SVGParsingException;

/**
 * The global configuration.
 *
 * @version 1.0
 */
public class GlobalConfig implements ExceptionsHandling {
   private static GlobalConfig config = null;
   private Boolean swingAvailable = null;
   private short exceptionsHandling = ExceptionsHandling.PRINT_EXCEPTION_MESSAGE;

   private GlobalConfig() {
   }

   /**
    * Return the unique instance.
    *
    * @return the unique instance
    */
   public static GlobalConfig getInstance() {
      if (config == null) {
         config = new GlobalConfig();
      }
      return config;
   }

   /**
    * Set the exceptions handling type.
    *
    * @param exceptionsHandling the exceptions handling type
    */
   public void setExceptionsHandling(short exceptionsHandling) {
      this.exceptionsHandling = exceptionsHandling;
   }

   /**
    * Return the exceptions handling type.
    *
    * @return the exceptions handling type
    */
   public short getExceptionsHandling() {
      return exceptionsHandling;
   }

   /**
    * Return true if swing is available.
    *
    * @return true if swing is available
    */
   public boolean isSwingAvailable() {
      if (swingAvailable == null) {
         try {
            Class.forName("io.fxtras.svg.AwtImageConverter", true, getClass().getClassLoader());
            swingAvailable = Boolean.TRUE;
         } catch (ClassNotFoundException ex) {
            swingAvailable = Boolean.FALSE;
         }
      }
      return swingAvailable;
   }

   /**
    * Handle an error message, depending on the value of the {@link #getExceptionsHandling()}. An exception will
    * be throw only if the value for the exceptions handling is {@link ExceptionsHandling#RETROW_EXCEPTION}.
    *
    * @param message the error message
    * @throws SVGLibraryException the rethrown exception
    */
   public void handleLibraryError(String message) throws SVGLibraryException {
      switch (exceptionsHandling) {
         case SKIP_EXCEPTION:
            return;
         case PRINT_EXCEPTION_MESSAGE:
            System.err.println(message);
            break;
         case RETROW_EXCEPTION:
            System.err.println(message);
            break;
         case PRINT_EXCEPTION_STACKTRACE:
            System.err.println(message);
            break;
         case RETROW_ALL:
            throw new SVGLibraryException(message);
         default:
            break;
      }
   }

   /**
    * Handle an error message, depending on the value of the {@link #getExceptionsHandling()}. An exception will
    * be throw only if the value for the exceptions handling is {@link ExceptionsHandling#RETROW_EXCEPTION}.
    *
    * @param message the error message
    * @throws SVGParsingException the rethrown exception
    */
   public void handleParsingError(String message) throws SVGParsingException {
      switch (exceptionsHandling) {
         case SKIP_EXCEPTION:
            return;
         case PRINT_EXCEPTION_MESSAGE:
            System.err.println(message);
            break;
         case RETROW_EXCEPTION:
            System.err.println(message);
            break;
         case PRINT_EXCEPTION_STACKTRACE:
            System.err.println(message);
            break;
         case RETROW_ALL:
            throw new SVGParsingException(message);
         default:
            break;
      }
   }

   /**
    * Handle a library exception, depending on the value of the {@link #getExceptionsHandling()}. An exception will
    * be throw only if the value for the exceptions handling is {@link ExceptionsHandling#RETROW_EXCEPTION}.
    *
    * @param th the Throwable
    * @throws SVGLibraryException the rethrown exception
    */
   public void handleLibraryException(Throwable th) throws SVGLibraryException {
      switch (exceptionsHandling) {
         case SKIP_EXCEPTION:
            return;
         case PRINT_EXCEPTION_MESSAGE:
            System.err.println(th.getMessage());
            break;
         case PRINT_EXCEPTION_STACKTRACE:
            th.printStackTrace();
            break;
         case RETROW_EXCEPTION:
            if (th instanceof SVGLibraryException) {
               throw (SVGLibraryException) th;
            } else {
               throw new SVGLibraryException(th);
            }
         default:
            break;
      }
   }

   /**
    * Handle a parsing exception, depending on the value of the {@link #getExceptionsHandling()}. An exception will
    * be throw only if the value for the exceptions handling is {@link ExceptionsHandling#RETROW_EXCEPTION}.
    *
    * @param th the Throwable
    * @throws SVGParsingException the rethrown exception
    */
   public void handleParsingException(Throwable th) throws SVGParsingException {
      switch (exceptionsHandling) {
         case SKIP_EXCEPTION:
            return;
         case PRINT_EXCEPTION_MESSAGE:
            System.err.println(th.getMessage());
            break;
         case PRINT_EXCEPTION_STACKTRACE:
            th.printStackTrace();
            break;
         case RETROW_EXCEPTION:
            if (th instanceof SVGLibraryException) {
               throw (SVGParsingException) th;
            } else {
               throw new SVGParsingException(th);
            }
         default:
            break;
      }
   }
}
