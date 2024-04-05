import { Chip } from "@nextui-org/react";
import Form from "./form";

export default function Home() {
  return (
    <main className="flex flex-col items-center">
      <div className="p-8 lg:p-24 flex flex-col items-center justify-center gap-8">
        <Chip size="lg" variant="flat" color="primary">
          Introducing TripFinder
        </Chip>
        <p className="font-bold text-4xl lg:text-6xl text-center text-balance">
          Find trips like you&apos;ve never done before.
        </p>
      </div>
      <Form />
    </main>
  );
}
