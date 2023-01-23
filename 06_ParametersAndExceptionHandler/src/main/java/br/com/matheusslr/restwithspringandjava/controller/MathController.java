package br.com.matheusslr.restwithspringandjava.controller;

import br.com.matheusslr.restwithspringandjava.service.MathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/math")
public class MathController {
    private final MathService mathService;

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping(path = "/sum/{numberOne}/{numberTwo}")
    public ResponseEntity<Double> sum(@PathVariable(value = "numberOne") String numberOne,
                                      @PathVariable(value = "numberTwo") String numberTwo) {
        return ResponseEntity.ok(this.mathService.sum(numberOne, numberTwo));
    }

    @GetMapping(path = "/sub/{numberOne}/{numberTwo}")
    public ResponseEntity<Double> sub(@PathVariable(value = "numberOne") String numberOne,
                                      @PathVariable(value = "numberTwo") String numberTwo) {
        return ResponseEntity.ok(this.mathService.sub(numberOne, numberTwo));
    }

    @GetMapping(path = "/mult/{numberOne}/{numberTwo}")
    public ResponseEntity<Double> mult(@PathVariable(value = "numberOne") String numberOne,
                                       @PathVariable(value = "numberTwo") String numberTwo) {
        return ResponseEntity.ok(this.mathService.mult(numberOne, numberTwo));
    }

    @GetMapping(path = "/div/{numberOne}/{numberTwo}")
    public ResponseEntity<Double> division(@PathVariable(value = "numberOne") String numberOne,
                                           @PathVariable(value = "numberTwo") String numberTwo) {
        return ResponseEntity.ok(this.mathService.division(numberOne, numberTwo));
    }

    @GetMapping(path = "/mean/{numberOne}/{numberTwo}")
    public ResponseEntity<Double> mean(@PathVariable(value = "numberOne") String numberOne,
                                       @PathVariable(value = "numberTwo") String numberTwo) {
        return ResponseEntity.ok(this.mathService.mean(numberOne, numberTwo));
    }

    @GetMapping(path = "/sqrt/{number}")
    public ResponseEntity<Double> mean(@PathVariable(value = "number") String number) {
        return ResponseEntity.ok(this.mathService.squareRoot(number));
    }
}
