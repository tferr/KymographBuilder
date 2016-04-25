/*
 * The MIT License
 *
 * Copyright 2016 Fiji.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fiji.plugin.kymographbuilder;

import ij.gui.Roi;
import java.util.List;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;

/**
 *
 * @author Hadrien Mary <hadrien.mary@gmail.com>
 */
public class KymographFactory {

    @Parameter
    private ImageJ ij;
    
    @Parameter
    private LogService log;

    @Parameter
    private Dataset dataset;

    private Roi roi;
    private List<Integer> channelsUsed;

    public KymographFactory(Context context, Roi roi, List<Integer> channelsUsed) {
        context.inject(this);
        this.channelsUsed = channelsUsed;
        this.roi = roi;
    }

    public void build() {
        // Build lines from the ROI
        LinesBuilder linesBuilder = new LinesBuilder(this.roi);
        linesBuilder.build();

        log.info(linesBuilder.getLines().size() + " lines with a width of "
                + linesBuilder.getlineWidth() + " will be used for the kymograph.");

        // Init kymo creator for each channels and build kymos
        for (Integer i : channelsUsed) {
            KymographCreator creator = new KymographCreator(ij.context(), i, linesBuilder);
            creator.build();
        }
    }

}