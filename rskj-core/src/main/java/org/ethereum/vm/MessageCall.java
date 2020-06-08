/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
 * (derived from ethereumJ library, Copyright (c) 2016 <ether.camp>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


package org.ethereum.vm;

/**
 * A wrapper for a message call from a contract to another account.
 * This can either be a normal CALL, CALLCODE, DELEGATECALL or POST call.
 */
public class MessageCall {

    public enum MsgType {
        CALL,
        CALLCODE,
        DELEGATECALL,
        STATICCALL,
        POST;

        /**
         *  Indicates that the code is executed in the context of the caller
         */
        public boolean isStateless() {
            return this == CALLCODE || this == DELEGATECALL;
        }

        public static MsgType fromOpcode(OpCode opCode) {
            switch (opCode) {
                case CALL: return CALL;
                case CALLCODE: return CALLCODE;
                case DELEGATECALL: return DELEGATECALL;
                case STATICCALL: return STATICCALL;
                default:
                    throw new RuntimeException("Invalid call opcode: " + opCode);
            }
        }
    }

    /**
     * Type of internal call. Either CALL, CALLCODE or POST
     */
    private final MsgType type;

    /**
     * gas to pay for the call, remaining gas will be refunded to the caller
     */
    private final DataWord gas;
    /**
     * storage rent gas to pay for the call, remaining rent gas will be refunded to the caller
     */
    //private final DataWord rentGas;
    /**
     * address of account which code to call
     */
    private final DataWord codeAddress;
    /**
     * the value that can be transfer along with the code execution
     */
    private final DataWord endowment;
    /**
     * start of memory to be input data to the call
     */
    private final DataWord inDataOffs;
    /**
     * size of memory to be input data to the call
     */
    private final DataWord inDataSize;
    /**
     * start of memory to be output of the call
     */
    private DataWord outDataOffs;
    /**
     * size of memory to be output data to the call
     */
    private DataWord outDataSize;

    // C1: #mish add rentgas field but not to arglist
    public MessageCall(MsgType type, DataWord gas, DataWord codeAddress,
                       DataWord endowment, DataWord inDataOffs, DataWord inDataSize) {
        this.type = type;
        this.gas = gas;
        this.codeAddress = codeAddress;
        this.endowment = endowment;
        this.inDataOffs = inDataOffs;
        this.inDataSize = inDataSize;
    }

    /*
    // C1A: same as C1, but now rentgas is in arglist
    public MessageCall(MsgType type, DataWord gas, DataWord rentGas, DataWord codeAddress,
                       DataWord endowment, DataWord inDataOffs, DataWord inDataSize) {
        this.type = type;
        this.gas = gas;
        this.rentGas = rentGas;
        this.codeAddress = codeAddress;
        this.endowment = endowment;
        this.inDataOffs = inDataOffs;
        this.inDataSize = inDataSize;
    }*/

    // #mish: C2 version before storage rent. Add rent field, but not to arglist. Use gas for rentgas
    public MessageCall(MsgType type, DataWord gas, DataWord codeAddress,
                       DataWord endowment, DataWord inDataOffs, DataWord inDataSize,
                       DataWord outDataOffs, DataWord outDataSize) {
        this(type, gas, codeAddress, endowment, inDataOffs, inDataSize);
        this.outDataOffs = outDataOffs;
        this.outDataSize = outDataSize;
    }

    /*
    // #mish: C2A Add rentgas to arglist.
    public MessageCall(MsgType type, DataWord gas, DataWord rentGas, DataWord codeAddress,
                       DataWord endowment, DataWord inDataOffs, DataWord inDataSize,
                       DataWord outDataOffs, DataWord outDataSize) {
        this(type, gas, rentGas, codeAddress, endowment, inDataOffs, inDataSize);
        this.outDataOffs = outDataOffs;
        this.outDataSize = outDataSize;
    }
    */

    public MsgType getType() {
        return type;
    }

    public DataWord getGas() {
        return gas;
    }

    /*
    public DataWord getRentGas() {
        return rentGas;
    }
    */

    public DataWord getCodeAddress() {
        return codeAddress;
    }

    public DataWord getEndowment() {
        return endowment;
    }

    public DataWord getInDataOffs() {
        return inDataOffs;
    }

    public DataWord getInDataSize() {
        return inDataSize;
    }

    public DataWord getOutDataOffs() {
        return outDataOffs;
    }

    public DataWord getOutDataSize() {
        return outDataSize;
    }
}
