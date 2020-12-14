package hr.fer.zemris.java.fractals;

import hr.fer.argparse.ArgParser;
import hr.fer.oprpp1.Complex;
import hr.fer.oprpp1.ComplexRootedPolynomial;
import hr.fer.oprpp1.NewtonFractalProducer;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.hw06.CommandParserFactory;
import hr.fer.zemris.java.hw06.ComplexCommand;
import hr.fer.zemris.java.hw06.Prompt;

public class NewtonParallel {
  public static void main(String[] args) {
    System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
    System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

    // Creating simple prompt with two commands: fallback and exit
    var promptParser = CommandParserFactory.getInstance().getDefaultPromptParser();
    var prompt = new Prompt("Root 1 > ", promptParser);
    var complexCmd = new ComplexCommand(CommandParserFactory.getInstance().complexNumberParser());
    prompt.registerFallbackCommand(complexCmd);
    prompt.registerExitCommand("done");

    // Parsing CLI arguments
    var argMap = new ArgParser().parse(args);
    var strTh = argMap.getOrDefault("workers", String.valueOf(Runtime.getRuntime().availableProcessors()));
    var strTr = argMap.getOrDefault("tracks", "4");
    var numTh = Integer.parseInt(strTh);
    var numTr = Integer.parseInt(strTr);

    // Running prompt
    prompt.executeCommand(prompt, null);
    // Getting entered polynomial
    complexCmd.getParsedNumbers();
    // Making fractal producer
    var crp = new ComplexRootedPolynomial(Complex.ONE, complexCmd.getParsedNumbers().toArray(Complex[]::new));
//    ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
//      Complex.ONE,
//      Complex.ONE,
//      Complex.ONE_NEG,
//      Complex.IM,
//      Complex.IM_NEG
//    );
    var nfp = new NewtonFractalProducer(crp, 0.001, 0.002);
    // Setting cli args
    nfp.setNumWorkers(numTh);
    nfp.setNumTracks(numTr);
    // Display
    FractalViewer.show(nfp);
  }
}
