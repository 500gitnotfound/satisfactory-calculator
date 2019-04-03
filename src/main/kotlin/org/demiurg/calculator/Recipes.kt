package org.demiurg.calculator

import org.demiurg.calculator.Component.*
import org.demiurg.calculator.Resource.*

val library = recipes {
    recipe(IronIngot, 1) {
        IronOre * 2
    }

    recipe(IronPlate, 4) {
        IronIngot * 2
    }

    recipe(IronRod, 4) {
        IronIngot * 1
    }

    recipe(CopperIngot, 2) {
        CopperOre * 1
    }

    recipe(Wire, 4, 3) {
        CopperIngot * 1
    }

    // TODO: Alternative
//    recipe(Wire, 3, 4) {
//        IronIngot * 2
//    }

    // TODO: Alternative
//    recipe(Wire, 9, 4) {
//        CateriumIngot * 1
//    }

    recipe(Cable, 4) {
        Wire * 2
    }

    // TODO: Alternative
//    recipe(Cable, 5, 4) {
//        Wire * 3
//        Rubber * 2
//    }

    recipe(Concrete, 4) {
        Limestone * 3
    }

    recipe(Screw, 6, 4) {
        IronRod * 1
    }

    // TODO: Alternative
//    recipe(Screw, 12, 4) {
//        IronIngot * 2
//    }

    recipe(ReinforcedIronPlate, 12) {
        IronPlate * 4
        Screw * 24
    }

    // TODO: Alternative
//    recipe(ReinforcedIronPlate, 3, 12) {
//        IronPlate * 6
//        Wire * 30
//    }

    // TODO: Alternative
//    recipe(ReinforcedIronPlate, 3, 12) {
//        IronPlate * 10
//        Screw * 24
//    }

    recipe(Rotor, 10) {
        IronRod * 3
        Screw * 22
    }

    recipe(ModularFrame, 15) {
        ReinforcedIronPlate * 3
        IronRod * 6
    }

    // TODO: Alternative
//    recipe(ModularFrame, 3, 15) {
//        ReinforcedIronPlate * 6
//        SteelPipe * 6
//    }

    recipe(SteelIngot, 2, 4) {
        IronOre * 3
        Coal * 3
    }

    // TODO: Alternative
//    recipe(SteelIngot, 6, 4) {
//        IronIngot * 3
//        Coal * 6
//    }

    recipe(SteelBeam, 6) {
        SteelIngot * 3
    }

    recipe(SteelPipe, 4) {
        SteelIngot * 1
    }

    recipe(EncasedIndustrialBeam, 15) {
        SteelBeam * 4
        Concrete * 5
    }

    // TODO: Alternative
//    recipe(EncasedIndustrialBeam, 3, 15) {
//        SteelPipe * 18
//        Concrete * 10
//    }

    recipe(Stator, 10) {
        SteelPipe * 3
        Wire * 10
    }

    // TODO: Alternative
//    recipe(Stator, 3, 10) {
//        SteelPipe * 6
//        Quickwire * 25
//    }

    recipe(Motor, 12) {
        Rotor * 2
        Stator * 2
    }

    recipe(HeavyModularFrame, 30) {
        ModularFrame * 5
        SteelPipe * 15
        EncasedIndustrialBeam * 5
        Screw * 90
    }

    // TODO: Alternative
//    recipe(HeavyModularFrame, 3, 30) {
//        ModularFrame * 8
//        SteelPipe * 36
//        EncasedIndustrialBeam * 10
//        Concrete * 25
//    }

    recipe(CateriumIngot, 4) {
        CateriumOre * 4
    }

    recipe(Quickwire, 4, 4) {
        CateriumIngot * 1
    }

    // TODO: Alternative
//    recipe(Quickwire, 12, 4) {
//        CateriumIngot * 1
//        CopperIngot * 2
//    }

    recipe(Plastic, 3, 8) {
        CrudeOil * 4
    }

    recipe(Rubber, 4, 8) {
        CrudeOil * 4
    }

    recipe(CircuitBoard, 12) {
        Wire * 12
        Plastic * 6
    }

    // TODO: Alternative
//    recipe(CircuitBoard, 3, 12) {
//        Rubber * 16
//        Wire * 24
//    }

    // TODO: Alternative
//    recipe(CircuitBoard, 3, 12) {
//        Plastic * 12
//        Quickwire * 32
//    }

    recipe(Computer, 32) {
        CircuitBoard * 5
        Cable * 12
        Plastic * 18
        Screw * 60
    }

    // TODO: Alternative
//    recipe(Computer, 3, 32) {
//        CircuitBoard * 10
//        Quickwire * 112
//        Rubber * 48
//    }

    recipe(AILimiter, 12) {
        CircuitBoard * 1
        Quickwire * 18
    }

    recipe(Supercomputer, 32) {
        Computer * 2
        AILimiter * 2
        HighSpeedConnector * 3
        Plastic * 21
    }

    recipe(HighSpeedConnector, 24) {
        Quickwire * 40
        Cable * 10
        Plastic * 6
    }
}